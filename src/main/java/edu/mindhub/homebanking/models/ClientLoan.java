package edu.mindhub.homebanking.models;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "client_loan")
@SQLDelete(sql = "UPDATE client_loan SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedClientLoan", parameters = @ParamDef(name = "deleted", type = "boolean"))
@Filter(name = "deletedClientLoan", condition = "deleted = :deleted")
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int payment;

    private double amount;

    private LocalDateTime date;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(int payment,
                      double amount,
                      LocalDateTime date) {
        this.payment = payment;
        this.amount = amount;
        this.date = date;
        this.deleted = false;
    }

    public long getId() {
        return id;
    }

    public int getPayments() {
        return payment;
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
        this.payment = payments;
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
