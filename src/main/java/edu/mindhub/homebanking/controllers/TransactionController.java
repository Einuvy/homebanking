package edu.mindhub.homebanking.controllers;

import edu.mindhub.homebanking.enums.TransactionType;
import edu.mindhub.homebanking.models.Account;
import edu.mindhub.homebanking.models.Client;
import edu.mindhub.homebanking.models.Transaction;
import edu.mindhub.homebanking.repositories.AccountRepository;
import edu.mindhub.homebanking.repositories.ClientRepository;
import edu.mindhub.homebanking.repositories.TransactionRepository;
import edu.mindhub.homebanking.services.implementations.AccountServiceImplementations;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientServiceImplementations clientService;
    @Autowired
    private AccountServiceImplementations accountService;
    @Autowired
    private TransactionServiceImplementations transactionService;


    @Transactional
    @PostMapping("/transactions")
    ResponseEntity <Object> createTransaction(Authentication authentication,
                                     @RequestParam Double amount,
                                     @RequestParam String description,
                                     @RequestParam String numberOrigin,
                                     @RequestParam String numberRecipients){

        Client currentClient =  clientService.findByEmail(authentication.getName());
        Account accountOrigin = accountService.findByNumber(numberOrigin);
        Account accountRecipients = accountService.findByNumber(numberRecipients);

        if (amount.isNaN() || amount <= 0 || amount.isInfinite()){
            return new ResponseEntity<>("Amount field cannot be empty", HttpStatus.FORBIDDEN);
        }else
        if (description.isEmpty()){
            return new ResponseEntity<>("Description field cannot be empty", HttpStatus.FORBIDDEN);
        }else
        if (numberOrigin.isEmpty()){
            return new ResponseEntity<>("Account origin field cannot be empty", HttpStatus.FORBIDDEN);
        }else
        if (numberRecipients.isEmpty()){
            return new ResponseEntity<>("Account recipients field cannot be empty", HttpStatus.FORBIDDEN);
        }else
        if (accountOrigin == accountRecipients){
            return new ResponseEntity<>("Choose a different recipient's account than the origin account", HttpStatus.FORBIDDEN);
        }else
        if(accountOrigin == null){
            return new ResponseEntity<>("Origin account does not exist", HttpStatus.FORBIDDEN);
        }else
        if(!accountOrigin.getClient().equals(currentClient)){
            return new ResponseEntity<>("Origin account is not the same as the authenticated client", HttpStatus.FORBIDDEN);
        }else
        if(accountRecipients == null){
            return new ResponseEntity<>("Recipients account does not exist", HttpStatus.FORBIDDEN);
        }else
        if (accountOrigin.getBalance() < amount){
            return new ResponseEntity<>("Your cash is not enough", HttpStatus.FORBIDDEN);
        }

        Transaction originTransaction = new Transaction(TransactionType.DEBIT, (amount*-1) ,description + " " + numberRecipients, LocalDateTime.now(), accountOrigin);
        Transaction recipientsTransaction = new Transaction(TransactionType.CREDIT, amount, description + " " + numberOrigin, LocalDateTime.now(), accountRecipients);

        accountOrigin.addTransaction(originTransaction);
        accountRecipients.addTransaction(recipientsTransaction);
        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountRecipients.setBalance(accountRecipients.getBalance() + amount);

        transactionService.saveTransaction(originTransaction);
        transactionService.saveTransaction(recipientsTransaction);

        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountRecipients);

        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }
}
