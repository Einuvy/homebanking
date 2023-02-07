package edu.mindhub.homebanking.models;


import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;


import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "client")
@SQLDelete(sql = "UPDATE client SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedUser", parameters = @ParamDef(name = "deleted", type = "boolean"))
@Filter(name = "deletedUser", condition = "deleted = :deleted")
public class Client {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy="client", fetch=EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "cardHolder", fetch = EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client(){
    }

    public Client(String firstName,
                  String lastName,
                  String email,
                  String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.deleted = false;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoans(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card){
        card.setCardHolder(this);
        cards.add(card);
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Client:" +
                "id=" + id +
                ", name=" + firstName + '\'' +
                ", surname=" + lastName + '\'' +
                ", email=" + email + '\'' +
                ", accounts=" + accounts ;
    }

}
