package edu.mindhub.homebanking.models;

import edu.mindhub.homebanking.enums.CardColor;
import edu.mindhub.homebanking.enums.CardType;
import lombok.Getter;
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
@Table(name = "card")
@SQLDelete(sql = "UPDATE card SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedCard", parameters = @ParamDef(name = "deleted", type = "boolean"))
@Filter(name = "deletedCard", condition = "deleted = :deleted")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(nullable = false)
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "card_holder_id")
    private Client cardHolder;

    @Enumerated(value = STRING)
    private CardType type;

    @Enumerated(value = STRING)
    private CardColor color;

    @Column(unique = true)
    private String number;

    private int cvv;

    private LocalDateTime thruDate;

    private LocalDateTime fromDate;

    @Column(name = "deleted")
    private Boolean deleted;

    public Card() {
    }

    public Card(CardType type,
                CardColor color,
                String number,
                int cvv,
                LocalDateTime fromDate,
                LocalDateTime thruDate) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.deleted = false;
    }

    public long getId() {
        return id;
    }

    public Client getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(Client cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
