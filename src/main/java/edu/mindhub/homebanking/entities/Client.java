package edu.mindhub.homebanking.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private String surname;
    private String email;

    @OneToMany(mappedBy="client", fetch=EAGER)
    private Set<Account> accounts = new HashSet<>();

    public Client() {
    }

    public Client(String name,
                  String surname,
                  String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    @Override
    public String toString() {
        return "Client:" +
                "id=" + id +
                ", name=" + name + '\'' +
                ", surname=" + surname + '\'' +
                ", email=" + email + '\'' +
                ", accounts=" + accounts ;
    }
}
