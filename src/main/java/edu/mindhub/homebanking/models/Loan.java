package edu.mindhub.homebanking.models;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "loan")
@SQLDelete(sql = "UPDATE loan SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedLoan", parameters = @ParamDef(name = "deleted", type = "boolean"))
@Filter(name = "deletedLoan", condition = "deleted = :deleted")
public class Loan {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private double maxAmount;

    private int percentaje;

    @Column(name = "deleted")
    private Boolean deleted;

    @ElementCollection
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan", fetch = EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name,
                double maxAmount,
                List<Integer> payments,
                int percentaje) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentaje = percentaje;
        this.deleted = false;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public int getPercentaje() {
        return percentaje;
    }

    public void setPercentaje(int percentaje) {
        this.percentaje = percentaje;
    }
}
