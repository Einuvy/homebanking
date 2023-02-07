package edu.mindhub.homebanking.dto;

import edu.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private List<AccountDTO> accounts;

    private List<ClientLoanDTO> loans;

    private List<CardDTO> cards;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().filter(account -> (!account.getDeleted())).map(account -> new AccountDTO(account)).collect(Collectors.toList());
        this.loans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
        this.cards = client.getCards().stream().filter(card -> (!card.getDeleted())).map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
