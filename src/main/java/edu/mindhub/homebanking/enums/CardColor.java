package edu.mindhub.homebanking.enums;

public enum CardColor {

    GOLD("Gold"),
    SILVER("Silver"),
    TITANIUM("Titanium");

    private String name;

    CardColor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
