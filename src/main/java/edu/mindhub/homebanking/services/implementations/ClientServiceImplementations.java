package edu.mindhub.homebanking.services.implementations;

import edu.mindhub.homebanking.models.Client;
import edu.mindhub.homebanking.repositories.ClientRepository;
import edu.mindhub.homebanking.services.ClientService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ClientServiceImplementations implements ClientService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client findByEmail(String email) {
        entityManager.unwrap(Session.class).enableFilter("deletedUser").setParameter("deleted", false);
        entityManager.unwrap(Session.class).enableFilter("deletedAccount").setParameter("deleted", false);
        Client clientAvailable = clientRepository.findByEmail(email);
        entityManager.unwrap(Session.class).disableFilter("deletedAccount");
        entityManager.unwrap(Session.class).disableFilter("deletedUser");

        return clientAvailable;
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<Client> findAllClients() {
        entityManager.unwrap(Session.class).enableFilter("deletedUser").setParameter("deleted", false);
        List<Client> clientsAvailable = clientRepository.findAll();
        entityManager.unwrap(Session.class).disableFilter("deletedUser");

        return clientsAvailable;
    }

    @Override
    public Client findById(Long id) {
        entityManager.unwrap(Session.class).enableFilter("deletedUser").setParameter("deleted", false);
        Client clientAvailable = clientRepository.findById(id).orElse(null);
        entityManager.unwrap(Session.class).disableFilter("deletedUser");

        return clientAvailable;
    }
}
