package edu.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int payments;

    private double amount;

    private LocalDateTime date;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(int payments,
                      double amount,
                      LocalDateTime date) {
        this.payments = payments;
        this.amount = amount;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public int getPayments() {
        return payments;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Client getClient() {
        return client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
