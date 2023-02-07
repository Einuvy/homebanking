package edu.mindhub.homebanking.services;

import edu.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<Account> findAllAccounts();

    Account findById(Long id);

    Account findByNumber(String number);

    void saveAccount(Account account);

    void deleteAccount(Account account);
}
