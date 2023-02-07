package edu.mindhub.homebanking.services;

import edu.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    Client findByEmail(String email);

    void saveClient(Client client);

    List<Client> findAllClients();

    Client findById(Long id);
}
