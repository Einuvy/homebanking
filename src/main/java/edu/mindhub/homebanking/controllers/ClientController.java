package edu.mindhub.homebanking.controllers;

import edu.mindhub.homebanking.dto.ClientDTO;
import edu.mindhub.homebanking.models.Account;
import edu.mindhub.homebanking.models.Client;
import edu.mindhub.homebanking.services.implementations.AccountServiceImplementations;
import edu.mindhub.homebanking.services.implementations.ClientServiceImplementations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientServiceImplementations clientService;
    @Autowired
    private AccountServiceImplementations accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAllClients().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.findById(id));
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing First Name", HttpStatus.FORBIDDEN);
        }else
        if (lastName.isEmpty()){
            return new ResponseEntity<>("Missing Last Name", HttpStatus.FORBIDDEN);
        }else
        if (email.isEmpty()){
            return new ResponseEntity<>("Missing Email", HttpStatus.FORBIDDEN);
        }else
        if (password.isEmpty()){
            return new ResponseEntity<>("Missing Password", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);

        Integer number =(int) (Math.random() * (99999999 - 10000000) + 10000000);
        String accountNumber = "VIN-" + number.toString();
        while (accountService.findByNumber(accountNumber) != null){
             number =(int) ((Math.random() * (99999999 - 10000000)) + 10000000);
             accountNumber = "VIN-" + number.toString();
        }

        Account account = new Account(accountNumber, LocalDateTime.now(), 0D);
        client.addAccount(account);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Client Created",HttpStatus.CREATED);
    }

    @GetMapping("/clients/account/send/{number}")
    public ClientDTO getSendClient(@PathVariable String number){ //Crear en el Repositorio un método para que me devuelva un client buscando por el Number de Account
        return new ClientDTO(accountService.findByNumber(number).getClient());
    }

    @GetMapping("clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }
}
