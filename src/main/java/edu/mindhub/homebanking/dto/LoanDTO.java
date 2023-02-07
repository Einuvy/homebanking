package edu.mindhub.homebanking.dto;

import edu.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.stream.Collectors;

public class LoanDTO {

    private long id;

    private String name;

    private double maxAmount;

    private List<Integer> payments;

    private List<ClientLoanDTO> clients;
    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.clients = loan.getClientLoans().stream().map(client -> new ClientLoanDTO(client)).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public List<ClientLoanDTO> getClients() {
        return clients;
    }
}
