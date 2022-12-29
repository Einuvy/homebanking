package edu.mindhub.homebanking.enums;

public enum CardType {
    CREDIT("Credit"),
    DEBIT("Debit");

    private String name;

    CardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
