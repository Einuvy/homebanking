package edu.mindhub.homebanking.dto;

public class LoanApplicationDTO {
    private Long id;
    private Double amount;
    private Integer payment;
    private String account;

    public Long getId() {
        return id;
    }
    public Double getAmount() {
        return amount;
    }
    public Integer getPayment() {
        return payment;
    }
    public String getAccount() {
        return account;
    }
}
