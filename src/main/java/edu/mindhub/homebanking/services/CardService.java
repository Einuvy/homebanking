package edu.mindhub.homebanking.services;

import edu.mindhub.homebanking.models.Card;

public interface CardService {
    Card findByNumber(String number);

    void saveCard(Card card);

    void deleteCard(Card card);
}
