package edu.mindhub.homebanking.controllers;

import edu.mindhub.homebanking.enums.CardColor;
import edu.mindhub.homebanking.enums.CardType;
import edu.mindhub.homebanking.models.Card;
import edu.mindhub.homebanking.models.Client;
import edu.mindhub.homebanking.repositories.CardRepository;
import edu.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardColor color,
                                             @RequestParam CardType type,
                                             Authentication authentication) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
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

        while (cardRepository.findByNumber(cardNumber) != null){
             cardNumber1 =(int) ((Math.random() * (9999 - 1000)) + 1000);
             cardNumber2 =(int) ((Math.random() * (9999 - 1000)) + 1000);
             cardNumber3 =(int) ((Math.random() * (9999 - 1000)) + 1000);
             cardNumber4 =(int) ((Math.random() * (9999 - 1000)) + 1000);

             cardNumber = cardNumber1.toString() + "-" + cardNumber2.toString() + "-" + cardNumber3.toString() + "-" + cardNumber4.toString();
        }

        Card card = new Card(type, color, cardNumber, cvv, creationDate, expirationDate);
        currentClient.addCard(card);
        cardRepository.save(card);

        return new ResponseEntity<>("You card has been created.",HttpStatus.CREATED);
    }
}
