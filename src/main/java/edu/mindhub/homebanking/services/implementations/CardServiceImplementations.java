package edu.mindhub.homebanking.services.implementations;


import edu.mindhub.homebanking.models.Account;
import edu.mindhub.homebanking.models.Card;
import edu.mindhub.homebanking.repositories.CardRepository;
import edu.mindhub.homebanking.services.CardService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class CardServiceImplementations implements CardService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CardRepository cardRepository;
    @Override
    public Card findByNumber(String number) {
        entityManager.unwrap(Session.class).enableFilter("deletedCard").setParameter("deleted", false);
        Card cardAvailable = cardRepository.findByNumber(number);
        entityManager.unwrap(Session.class).disableFilter("deletedCard");
        return cardAvailable;
    }
    @Override
    public void deleteCard(Card card){
        cardRepository.delete(card);
    }
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
