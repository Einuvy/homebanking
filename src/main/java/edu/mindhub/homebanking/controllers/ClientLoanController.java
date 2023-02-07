package edu.mindhub.homebanking.controllers;

import edu.mindhub.homebanking.enums.TransactionType;
import edu.mindhub.homebanking.models.Account;
import edu.mindhub.homebanking.models.Client;
import edu.mindhub.homebanking.models.ClientLoan;
import edu.mindhub.homebanking.models.Transaction;
import edu.mindhub.homebanking.repositories.AccountRepository;
import edu.mindhub.homebanking.repositories.ClientLoanRepository;
import edu.mindhub.homebanking.repositories.ClientRepository;
import edu.mindhub.homebanking.repositories.TransactionRepository;
import edu.mindhub.homebanking.services.implementations.AccountServiceImplementations;
import edu.mindhub.homebanking.services.implementations.ClientLoanServiceImplementations;
import edu.mindhub.homebanking.services.implementations.ClientServiceImplementations;
import edu.mindhub.homebanking.services.implementations.TransactionServiceImplementations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    private ClientLoanServiceImplementations clientLoanService;
    @Autowired
    private ClientServiceImplementations clientService;
    @Autowired
    private AccountServiceImplementations accountService;
    @Autowired
    private TransactionServiceImplementations transactionService;

    @PostMapping("/payloan")
    ResponseEntity<Object> payLoan(Authentication authentication,
                                   @RequestParam Long clientLoanId,
                                   @RequestParam Double amount,
                                   @RequestParam String account){

        Client currentClient = clientService.findByEmail(authentication.getName());
        ClientLoan thisLoan = clientLoanService.findById(clientLoanId);
        Account currentAccount = accountService.findByNumber(account);
        if (amount.isNaN() || amount.isInfinite()|| amount ==0){
            return new ResponseEntity<>("Amount field cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (currentClient == null){
            return new ResponseEntity<>("You need login first", HttpStatus.FORBIDDEN);
        }
        if (thisLoan == null){
            return new ResponseEntity<>("Loan field cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (currentAccount == null){
            return new ResponseEntity<>("Accoun field cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (currentAccount.getClient() != currentClient){
            return new ResponseEntity<>("This account dosn't match with your accounts",HttpStatus.FORBIDDEN);
        }
        if (thisLoan.getClient() != currentClient){
            return new ResponseEntity<>("This loan dosn't match with your account",HttpStatus.FORBIDDEN);
        }
        if (thisLoan.getClient().getAccounts().stream().filter(clientAccount -> clientAccount.equals(currentAccount)).collect(Collectors.toList()).isEmpty()){
            return new ResponseEntity<>("This account dosn't match with account debtor",HttpStatus.FORBIDDEN);
        }
        if (thisLoan.getAmount()==0){
            return new ResponseEntity<>("This loan was previously paid", HttpStatus.FORBIDDEN);
        }
        if (amount>thisLoan.getAmount()){
            return new ResponseEntity<>("The amount is greater than the necessary payment", HttpStatus.FORBIDDEN);
        }
        if (amount>(thisLoan.getAmount()/ thisLoan.getPayments())){
            return new ResponseEntity<>("The amount is greater than the necessary payment", HttpStatus.FORBIDDEN);
        }
        if(amount>currentAccount.getBalance()){
            return new ResponseEntity<>("Your cash is not enough", HttpStatus.FORBIDDEN);
        }
        thisLoan.setAmount(thisLoan.getAmount()-amount);
        thisLoan.setPayments(thisLoan.getPayments()-1);

        Transaction transaction = new Transaction(TransactionType.DEBIT, amount*-1, " Loan payment '" + thisLoan.getLoan().getName() + "' " + currentAccount.getNumber(), LocalDateTime.now(), currentAccount);
        currentAccount.addTransaction(transaction);
        currentAccount.setBalance(currentAccount.getBalance() - amount);

        clientLoanService.saveClientLoan(thisLoan);
        accountService.saveAccount(currentAccount);
        transactionService.saveTransaction(transaction);
        return new ResponseEntity<>("The payment has been successfully", HttpStatus.OK);
    }
}
