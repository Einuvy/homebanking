package edu.mindhub.homebanking.dto;

public class CardTransactionDTO {

    private String number;
    private Double amount;
    private int cvv;
    private String decription;

    public String getNumber() {
        return number;
    }

    public Double getAmount() {
        return amount;
    }

    public int getCvv() {
        return cvv;
    }

    public String getDecription() {
        return decription;
    }
}
