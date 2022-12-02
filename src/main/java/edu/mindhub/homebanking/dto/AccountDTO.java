package edu.mindhub.homebanking.dto;

import edu.mindhub.homebanking.entities.Account;

import java.time.LocalDateTime;

public class AccountDTO {

    private long id;

    private String number;

    private LocalDateTime creationDate;

    private double balance;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

}
