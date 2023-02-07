package edu.mindhub.homebanking.services.implementations;

import edu.mindhub.homebanking.models.Account;
import edu.mindhub.homebanking.repositories.AccountRepository;
import edu.mindhub.homebanking.services.AccountService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service
public class AccountServiceImplementations implements AccountService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> findAllAccounts() {
        entityManager.unwrap(Session.class).enableFilter("deletedAccount").setParameter("deleted", false);
        List <Account> accountsAvailable = accountRepository.findAll();
        entityManager.unwrap(Session.class).disableFilter("deletedAccount");

        return accountsAvailable;
    }

    @Override
    public Account findById(Long id) {
        entityManager.unwrap(Session.class).enableFilter("deletedAccount").setParameter("deleted", false);
        Account accountAvailable = accountRepository.findById(id).orElse(null);
        entityManager.unwrap(Session.class).disableFilter("deletedAccount");

        return accountAvailable;
    }

    @Override
    public Account findByNumber(String number) {
        entityManager.unwrap(Session.class).enableFilter("deletedAccount").setParameter("deleted", false);
        Account accountAvailable = accountRepository.findByNumber(number);
        entityManager.unwrap(Session.class).disableFilter("deletedAccount");

        return accountAvailable;
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }
}
