package edu.mindhub.homebanking.services;

import edu.mindhub.homebanking.models.ClientLoan;

public interface ClientLoanService {

    ClientLoan findById(Long id);

    void saveClientLoan(ClientLoan clientLoan);

}
