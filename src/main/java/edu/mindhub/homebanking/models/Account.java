package edu.mindhub.homebanking.models;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "account")
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedAccount", parameters = @ParamDef(name = "deleted", type = "boolean"))
@Filter(name = "deletedAccount", condition = "deleted = :deleted")
public class Account {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column(unique = true)
    private String number;

    private LocalDateTime creationDate;

    private double balance;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch= EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.deleted = false;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Account:" +
                "id=" + id +
                ", number=" + number + '\'';
    }


}
