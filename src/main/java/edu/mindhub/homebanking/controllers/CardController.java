package edu.mindhub.homebanking.controllers;

import edu.mindhub.homebanking.dto.CardTransactionDTO;
import edu.mindhub.homebanking.enums.CardColor;
import edu.mindhub.homebanking.enums.CardType;
import edu.mindhub.homebanking.enums.TransactionType;
import edu.mindhub.homebanking.models.Account;
import edu.mindhub.homebanking.models.Card;
import edu.mindhub.homebanking.models.Client;
import edu.mindhub.homebanking.models.Transaction;
import edu.mindhub.homebanking.services.TransactionService;
import edu.mindhub.homebanking.services.implementations.AccountServiceImplementations;
import edu.mindhub.homebanking.services.implementations.CardServiceImplementations;
import edu.mindhub.homebanking.services.implementations.ClientServiceImplementations;
import edu.mindhub.homebanking.services.implementations.TransactionServiceImplementations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientServiceImplementations clientService;
    @Autowired
    private CardServiceImplementations cardService;
    @Autowired
    private TransactionServiceImplementations transactionService;
    @Autowired
    private AccountServiceImplementations accountService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardColor color,
                                             @RequestParam CardType type,
                                             Authentication authentication) {

        Client currentClient = clientService.findByEmail(authentication.getName());
        if (color.equals("") || color == null || color.getName() == null || color.getName().equals("")){
            return new ResponseEntity<>("Error card color field is empty", HttpStatus.FORBIDDEN);
        }
        if (type.equals("") || type == null || type.getName() == null || type.getName().equals("")){
            return new ResponseEntity<>("Error card type field is empty", HttpStatus.FORBIDDEN);
        }
        if(!currentClient.getCards().stream().filter(card -> card.getColor().equals(color))
                .collect(Collectors.toSet()).stream().filter(card -> card.getType().equals(type))
                .collect(Collectors.toList()).isEmpty()){
            return new ResponseEntity<>("You already have max amount of this card", HttpStatus.FORBIDDEN);
        }

        Integer cvv =(int) ((Math.random() * (999 - 100)) + 100);
        Integer cardNumber1 =(int) ((Math.random() * (9999 - 1000)) + 1000);
        Integer cardNumber2 =(int) ((Math.random() * (9999 - 1000)) + 1000);
        Integer cardNumber3 =(int) ((Math.random() * (9999 - 1000)) + 1000);
        Integer cardNumber4 =(int) ((Math.random() * (9999 - 1000)) + 1000);

        String cardNumber = cardNumber1.toString() + "-" + cardNumber2.toString() + "-" + cardNumber3.toString() + "-" + cardNumber4.toString();
        String cardHolder = currentClient.getFirstName() + " " + currentClient.getLastName();

        LocalDateTime creationDate = LocalDateTime.now();
        LocalDateTime expirationDate = LocalDateTime.now().plusYears(5);

        while (cardService.findByNumber(cardNumber) != null){
             cardNumber1 =(int) ((Math.random() * (9999 - 1000)) + 1000);
             cardNumber2 =(int) ((Math.random() * (9999 - 1000)) + 1000);
             cardNumber3 =(int) ((Math.random() * (9999 - 1000)) + 1000);
             cardNumber4 =(int) ((Math.random() * (9999 - 1000)) + 1000);

             cardNumber = cardNumber1.toString() + "-" + cardNumber2.toString() + "-" + cardNumber3.toString() + "-" + cardNumber4.toString();
        }

        Card card = new Card(type, color, cardNumber, cvv, creationDate, expirationDate);
        currentClient.addCard(card);
        cardService.saveCard(card);
        clientService.saveClient(currentClient);

        return new ResponseEntity<>("You card has been created.",HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/clients/current/card/transaction")
    public ResponseEntity<Object> transactionWithCard(@RequestBody CardTransactionDTO cardTransaction){
        Double amount = cardTransaction.getAmount();
        String cardNumber = cardTransaction.getNumber();
        Card currentCard = cardService.findByNumber(cardNumber);
        Client currentClient = currentCard.getCardHolder();
        Set setAccountsWithBalance = currentClient.getAccounts().stream().filter(account -> account.getBalance() < amount).collect(Collectors.toSet());
        Account firstAccount = currentClient.getAccounts().stream().min(Comparator.comparing(account -> account.getId())).orElse(null);

        if(cardTransaction.getAmount().isNaN() || cardTransaction.getAmount().isInfinite() || cardTransaction.getAmount() <= 0){
            return new ResponseEntity<>("Error wrong amount", HttpStatus.FORBIDDEN);
        }
        if(currentCard == null){
            return new ResponseEntity<>("Error card not found", HttpStatus.FORBIDDEN);
        }
        if (setAccountsWithBalance.isEmpty()){
            return new ResponseEntity<>("Error not enough money", HttpStatus.FORBIDDEN);
        }
        if (currentCard.getCvv() != cardTransaction.getCvv()){
            return new ResponseEntity<>("Error wrong cvv", HttpStatus.FORBIDDEN);
        }
        if (currentCard.getThruDate().isAfter(LocalDateTime.now())){
            return new ResponseEntity<>("Error expired card", HttpStatus.FORBIDDEN);
        }

        Transaction myCardTransaction = new Transaction(TransactionType.DEBIT, (amount*-1) ,cardTransaction.getDecription() , LocalDateTime.now(), firstAccount);
        firstAccount.addTransaction(myCardTransaction);
        firstAccount.setBalance(firstAccount.getBalance() - amount);

        transactionService.saveTransaction(myCardTransaction);
        accountService.saveAccount(firstAccount);

        return new ResponseEntity<>("Payment successfully", HttpStatus.OK);
    }

    @DeleteMapping("/clients/current/card")
    public ResponseEntity<Object> deleteCard(@RequestParam String cardNumber,
                                             Authentication authentication){

        Card currentCard = cardService.findByNumber(cardNumber);
        Client currentClient = clientService.findByEmail(authentication.getName());

        if (cardNumber.equals("") || cardNumber.isEmpty() || cardNumber == null ){
            return new ResponseEntity<>("Card number wrong" ,HttpStatus.FORBIDDEN);
        }
        if (currentCard == null){
            return new ResponseEntity<>("Card number wrong" ,HttpStatus.FORBIDDEN);
        }
        if (currentClient == null){
            return new ResponseEntity<>("Authentication error" ,HttpStatus.FORBIDDEN);
        }
        if (currentClient.getCards().stream().filter(card -> card.getNumber().equals(currentCard.getNumber())).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("Card number wrong" , HttpStatus.FORBIDDEN);
        }

        cardService.deleteCard(currentCard);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
