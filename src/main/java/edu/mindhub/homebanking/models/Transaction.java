package edu.mindhub.homebanking.models;

import edu.mindhub.homebanking.enums.TransactionType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "transaction")
@SQLDelete(sql = "UPDATE transaction SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedTransaction", parameters = @ParamDef(name = "deleted", type = "boolean"))
@Filter(name = "deletedTransaction", condition = "deleted = :deleted")
public class Transaction {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Enumerated(value = STRING)
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    @Column(name = "deleted")
    private Boolean deleted;
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(TransactionType type,
                       double amount,
                       String description,
                       LocalDateTime date,
                       Account account) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        this.deleted = false;
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", account=" + account +
                '}';
    }
}
