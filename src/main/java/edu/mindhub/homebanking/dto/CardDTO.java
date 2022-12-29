package edu.mindhub.homebanking.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import edu.mindhub.homebanking.models.Card;
import java.time.LocalDateTime;

public class CardDTO {

    private long id;

    private String cardHolder;

    private String type;

    private String color;

    private String number;

    private int cvv;

    @JsonFormat(pattern="yyyy/MM")
    private LocalDateTime thruDate;

    @JsonFormat(pattern="yyyy/MM")
    private LocalDateTime fromDate;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder().getFirstName() + " " + card.getCardHolder().getLastName();
        this.type = card.getType().getName();
        this.color = card.getColor().getName();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }
}
