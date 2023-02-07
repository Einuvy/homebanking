package edu.mindhub.homebanking.services.implementations;

import edu.mindhub.homebanking.models.ClientLoan;
import edu.mindhub.homebanking.repositories.ClientLoanRepository;
import edu.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplementations implements ClientLoanService {
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public ClientLoan findById(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
