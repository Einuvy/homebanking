package edu.mindhub.homebanking.controllers;
import com.itextpdf.text.DocumentException;
import edu.mindhub.homebanking.dto.AccountDTO;
import edu.mindhub.homebanking.dto.ClientDTO;
import edu.mindhub.homebanking.dto.TransactionDTO;
import edu.mindhub.homebanking.models.Account;
import edu.mindhub.homebanking.models.Card;
import edu.mindhub.homebanking.models.Client;
import edu.mindhub.homebanking.models.Transaction;
import edu.mindhub.homebanking.repositories.AccountRepository;
import edu.mindhub.homebanking.repositories.ClientRepository;
import edu.mindhub.homebanking.services.implementations.AccountServiceImplementations;
import edu.mindhub.homebanking.services.implementations.ClientServiceImplementations;
import edu.mindhub.homebanking.services.implementations.PDFGeneratorServiceImplementations;
import edu.mindhub.homebanking.services.implementations.TransactionServiceImplementations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private ClientServiceImplementations clientService;
    @Autowired
    private AccountServiceImplementations accountService;

    @Autowired
    private TransactionServiceImplementations transactionService;

    @Autowired
    private PDFGeneratorServiceImplementations pdfGeneratorService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAllAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.findById(id));
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        Client currentClient = clientService.findByEmail(authentication.getName());

        if(currentClient.getAccounts().size() >= 3){
            return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
        }

        Integer number =(int) ((Math.random() * (99999999 - 10000000)) + 10000000);
        String accountNumber = "VIN-" + number.toString();

        while (accountService.findByNumber(accountNumber) != null){
            number =(int) ((Math.random() * (99999999 - 10000000)) + 10000000);
            accountNumber = "VIN-" + number.toString();
        }

        Account account = new Account(accountNumber, LocalDateTime.now(), 0D);
        currentClient.addAccount(account);
        accountService.saveAccount(account);
        clientService.saveClient(currentClient);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/account")
    public ResponseEntity<Object> deleteAccount(@RequestParam String accountNumber,
                                             Authentication authentication){

        Account currentAccount = accountService.findByNumber(accountNumber);
        Client currentClient = clientService.findByEmail(authentication.getName());

        if (accountNumber.equals("") || accountNumber.isEmpty() || accountNumber == null ){
            return new ResponseEntity<>("Account number wrong" ,HttpStatus.FORBIDDEN);
        }
        if (currentAccount == null){
            return new ResponseEntity<>("Account number wrong" ,HttpStatus.FORBIDDEN);
        }
        if (currentClient == null){
            return new ResponseEntity<>("Authentication error" ,HttpStatus.FORBIDDEN);
        }
        if (currentClient.getAccounts().stream().filter(account -> account.getNumber().equals(currentAccount.getNumber())).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("Account number wrong" , HttpStatus.FORBIDDEN);
        }
        if(currentAccount.getBalance() != 0){
            return new ResponseEntity<>("Your account balance isn't 0", HttpStatus.FORBIDDEN);
        }

        currentAccount.getTransactions().forEach(transaction -> transactionService.deleteTransaction(transaction));
        accountService.deleteAccount(currentAccount);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/accounts/pdf/generate")
    public void generatePDF(Authentication authentication,
                            HttpServletResponse response,
                            @RequestParam String initDate,
                            @RequestParam String endDate,
                            @RequestParam String accountNumber) throws DocumentException, IOException {

        pdfGeneratorService.generatePDF(response);
        pdfGeneratorService.addTitle("My transactions");
        pdfGeneratorService.addSpace();
        pdfGeneratorService.addParagraph("From " + initDate + " to " + endDate);
        pdfGeneratorService.addSpace();

        Account currentAccount = accountService.findByNumber(accountNumber);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        DateTimeFormatter formatterFileName = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDateTime.parse(initDate + ".000000", formatter);
        LocalDateTime finalDate = LocalDateTime.parse(endDate + ".000000", formatter);

        Set<Transaction> printedTransactions = currentAccount.getTransactions().stream().filter(transaction -> (transaction.getDate().isEqual(startDate) || transaction.getDate().isAfter(startDate) && transaction.getDate().isEqual(finalDate) || transaction.getDate().isBefore(finalDate))).collect(Collectors.toSet());

        response.setContentType("application/pdf");

        String headerValue = "attacment; filename=transactions_"+LocalDateTime.now().format(formatterFileName)+"-account" + currentAccount.getNumber()+".pdf";
        response.setHeader("Content-Disposition", headerValue);



        pdfGeneratorService.addAccount(new AccountDTO(currentAccount));
        pdfGeneratorService.addSpace();
        pdfGeneratorService.addTransaction(printedTransactions);
        pdfGeneratorService.closeDocument();
    }
}
