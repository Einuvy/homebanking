package edu.mindhub.homebanking.controllers;

import edu.mindhub.homebanking.dto.LoanApplicationDTO;
import edu.mindhub.homebanking.dto.LoanDTO;
import edu.mindhub.homebanking.enums.TransactionType;
import edu.mindhub.homebanking.models.*;
import edu.mindhub.homebanking.repositories.*;
import edu.mindhub.homebanking.services.AccountService;
import edu.mindhub.homebanking.services.ClientLoanService;
import edu.mindhub.homebanking.services.LoanService;
import edu.mindhub.homebanking.services.TransactionService;
import edu.mindhub.homebanking.services.implementations.AccountServiceImplementations;
import edu.mindhub.homebanking.services.implementations.ClientLoanServiceImplementations;
import edu.mindhub.homebanking.services.implementations.ClientServiceImplementations;
import edu.mindhub.homebanking.services.implementations.LoanServiceImplementations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Double.*;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanServiceImplementations loanService;
    @Autowired
    private AccountServiceImplementations accountService;
    @Autowired
    private ClientServiceImplementations clientService;
    @Autowired
    private ClientLoanServiceImplementations clientLoanService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity <Object> createLoan(Authentication authentication,
                                             @RequestBody LoanApplicationDTO loan){

       Loan thisLoan = loanService.findById(loan.getId());
       Account account = accountService.findByNumber(loan.getAccount());
       Client currentClient = clientService.findByEmail(authentication.getName());
       Set<Loan> loansCurrentClient = currentClient.getClientLoans().stream().map(clientLoans -> clientLoans.getLoan()).collect(Collectors.toSet());

        if(thisLoan == null){
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }
        if(account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }
        if(loan.getAmount() <= 0 || loan.getAmount() == null){
            return new ResponseEntity<>("Amount field cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (loan.getAmount() > thisLoan.getMaxAmount()){
            return new ResponseEntity<>("Amount field cannot exceed max amount", HttpStatus.FORBIDDEN);
        }
        if(loan.getPayment() <= 0 || thisLoan.getPayments().stream().filter(payment -> payment.equals(loan.getPayment())).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("Payment not found", HttpStatus.FORBIDDEN);
        }
        if(loan.getId() == 0 || Double.isNaN(loan.getId()) || loan.getId() == NEGATIVE_INFINITY ||  loan.getId() == POSITIVE_INFINITY){
            return new ResponseEntity<>("Loan field cannot be empty", HttpStatus.FORBIDDEN);
        }
        if(loan.getAccount() == null || loan.getAccount().isEmpty()){
            return new ResponseEntity<>("Account field cannot be empty", HttpStatus.FORBIDDEN);
        }
        if(currentClient.getAccounts().stream().filter(clientAccount -> clientAccount.equals(account)).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("Account destinatary dosn't match with your accounts", HttpStatus.FORBIDDEN);
        }
        if (loansCurrentClient.contains(thisLoan)){
            return new ResponseEntity<>("You already have a loan of this type", HttpStatus.FORBIDDEN);
        }

        Double clientLoanPercentaje = (loan.getAmount()* thisLoan.getPercentaje())/100;

        ClientLoan clientLoan = new ClientLoan(loan.getPayment(), loan.getAmount()+clientLoanPercentaje, LocalDateTime.now());
        clientLoan.setClient(currentClient);
        clientLoan.setLoan(thisLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loan.getAmount(), " Loan " + thisLoan.getName() + "approved", LocalDateTime.now(), account);
        account.addTransaction(transaction);
        account.setBalance(account.getBalance() + loan.getAmount());

        clientLoanService.saveClientLoan(clientLoan);
        accountService.saveAccount(account);
        transactionService.saveTransaction(transaction);

        return new ResponseEntity<>("Your loan has been approved",HttpStatus.CREATED);
   }

    @GetMapping("/loans")
   public List<LoanDTO> getLoans(){
        return loanService.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
   }

   @PostMapping("/loan/create")
    public ResponseEntity<Object> createLoan(@RequestParam String name,
                                             @RequestParam Double maxAmount,
                                             @RequestParam List<Integer> payments,
                                             @RequestParam Integer percentaje){

        if (name.isEmpty()||name.length()<=0||name.equals("")){
            return new ResponseEntity<>("Name is missing", HttpStatus.FORBIDDEN);
        }
        if (maxAmount.isInfinite()|| maxAmount.isNaN() || maxAmount<=0){
            return new ResponseEntity<>("Max amount is missing", HttpStatus.FORBIDDEN);
        }
       if (payments.isEmpty() || payments.size()<=0){
           return new ResponseEntity<>("Payments is missing", HttpStatus.FORBIDDEN);
       }
       if (percentaje <=0){
           return new ResponseEntity<>("Percentaje is missing", HttpStatus.FORBIDDEN);
       }

       Loan loan = new Loan(name, maxAmount, payments, percentaje);
       loanService.saveLoan(loan);

        return new ResponseEntity<>("Loan Created", HttpStatus.OK);
   }
}
