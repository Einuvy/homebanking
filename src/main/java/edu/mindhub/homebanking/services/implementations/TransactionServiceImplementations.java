package edu.mindhub.homebanking.services.implementations;

import edu.mindhub.homebanking.models.Transaction;
import edu.mindhub.homebanking.repositories.TransactionRepository;
import edu.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplementations implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
}
