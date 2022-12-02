package edu.mindhub.homebanking;

import edu.mindhub.homebanking.entities.Account;
import edu.mindhub.homebanking.entities.Client;
import edu.mindhub.homebanking.repositories.AccountRepository;
import edu.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client client = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(client);

			Client client2 = new Client("Nahuel", "Morel", "manu@mindhub.com");
			clientRepository.save(client2);

			Client client3 = new Client("Sebastian", "Morel", "seba@mindhub.com");
			clientRepository.save(client3);

			Client client4 = new Client("Martin", "Morel", "martin@mindhub.com");
			clientRepository.save(client4);


			Account ASD35001 = new Account("ASD35001", LocalDateTime.now(), 5000d);
			client.addAccount(ASD35001);
			accountRepository.save(ASD35001);

			Account ASD35002 = new Account("ASD35002", LocalDateTime.now().plusDays(1), 7500d);
			client.addAccount(ASD35002);
			accountRepository.save(ASD35002);

			Account ASD35003 = new Account("ASD35003", LocalDateTime.now(), 7565d);
			client2.addAccount(ASD35003);
			accountRepository.save(ASD35003);

			Account ASD35004 = new Account("ASD35004", LocalDateTime.now().plusDays(1), 9500d);
			client2.addAccount(ASD35004);
			accountRepository.save(ASD35004);

			Account ASD35005 = new Account("ASD35005", LocalDateTime.now(), 750895d);
			client3.addAccount(ASD35005);
			accountRepository.save(ASD35005);

			Account ASD35006 = new Account("ASD35006", LocalDateTime.now().minusMonths(5), -760000d);
			client3.addAccount(ASD35006);
			accountRepository.save(ASD35006);

			Account ASD35007 = new Account("ASD35003", LocalDateTime.now(), 1d);
			client4.addAccount(ASD35007);
			accountRepository.save(ASD35007);

			Account ASD35008 = new Account("ASD35004", LocalDateTime.now().minusYears(1), 0.50d);
			client4.addAccount(ASD35008);
			accountRepository.save(ASD35008);



		};
	}
}
