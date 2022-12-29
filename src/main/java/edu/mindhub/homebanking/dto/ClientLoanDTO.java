package edu.mindhub.homebanking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.mindhub.homebanking.models.ClientLoan;

import java.time.LocalDateTime;

public class ClientLoanDTO {

    private long id;

    private Long loanId;

    private String name;

    private double amount;

    private int payments;

    @JsonFormat(pattern="yyyy/MM/dd - HH:mm:ss")
    private LocalDateTime date;


    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.payments = clientLoan.getPayments();
        this.amount = clientLoan.getAmount();
        this.date = clientLoan.getDate();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
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

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }
}
