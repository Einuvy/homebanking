package edu.mindhub.homebanking.services.implementations;

import edu.mindhub.homebanking.models.Loan;
import edu.mindhub.homebanking.repositories.LoanRepository;
import edu.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImplementations implements LoanService {
    @Autowired
    LoanRepository loanRepository;

    @Override
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
