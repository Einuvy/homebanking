package edu.mindhub.homebanking.controllers;

import edu.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    private ClientRepository clientRepository;
}
