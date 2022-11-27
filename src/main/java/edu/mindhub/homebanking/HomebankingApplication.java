package edu.mindhub.homebanking;

import edu.mindhub.homebanking.entities.Client;
import edu.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository) {
		return (args) -> {
			Client client = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(client);
			Client client2 = new Client("Nahuel", "Morel", "manu@mindhub.com");
			clientRepository.save(client2);
			Client client3 = new Client("Sebastian", "Morel", "seba@mindhub.com");
			clientRepository.save(client3);
			Client client4 = new Client("Martin", "Morel", "martin@mindhub.com");
			clientRepository.save(client4);

		};
	}
}
