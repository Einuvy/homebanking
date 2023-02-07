package edu.mindhub.homebanking.services;

import edu.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    Loan findById(Long id);

    List<Loan> findAll();

    void saveLoan(Loan loan);
}
