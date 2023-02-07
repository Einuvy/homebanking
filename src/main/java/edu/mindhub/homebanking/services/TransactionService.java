package edu.mindhub.homebanking.services;

import edu.mindhub.homebanking.models.Transaction;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    void deleteTransaction(Transaction transaction);
}
