package edu.mindhub.homebanking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;

public class TransactionDTO {

    private long id;

    private String type;
    private double amount;
    private String description;
    @JsonFormat(pattern="yyyy/MM/dd - HH:mm:ss")
    private LocalDateTime date;


    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType().getName();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }


}
