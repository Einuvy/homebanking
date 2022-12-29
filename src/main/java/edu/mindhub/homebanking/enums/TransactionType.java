package edu.mindhub.homebanking.enums;

public enum TransactionType {
    DEBIT("Debit"),
    CREDIT("Credit");

    private String name;

    TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}