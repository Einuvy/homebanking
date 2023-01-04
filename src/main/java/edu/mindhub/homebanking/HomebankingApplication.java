package edu.mindhub.homebanking;

import edu.mindhub.homebanking.enums.CardColor;
import edu.mindhub.homebanking.enums.CardType;
import edu.mindhub.homebanking.models.*;
import edu.mindhub.homebanking.enums.TransactionType;
import edu.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return (args) -> {
			Client client = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("asd005"));
			clientRepository.save(client);

			Client client2 = new Client("Nahuel", "Morel", "manu@admin.com", passwordEncoder.encode("hola123"));
			clientRepository.save(client2);

			Client client3 = new Client("Sebastian", "Morel", "seba@mindhub.com", passwordEncoder.encode("otrogato"));
			clientRepository.save(client3);

			Client client4 = new Client("Martin", "Morel", "martin@mindhub.com",passwordEncoder.encode("buendia"));
			clientRepository.save(client4);


			Account VIN001 = new Account("VIN001", LocalDateTime.now(), 5000d);
			client.addAccount(VIN001);
			accountRepository.save(VIN001);

			Account VIN002 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500d);
			client.addAccount(VIN002);
			accountRepository.save(VIN002);

			Account VIN003 = new Account("VIN003", LocalDateTime.now(), 7565d);
			client2.addAccount(VIN003);
			accountRepository.save(VIN003);

			Account VIN004 = new Account("VIN004", LocalDateTime.now().plusDays(1), 9500d);
			client2.addAccount(VIN004);
			accountRepository.save(VIN004);

			Account VIN005 = new Account("VIN005", LocalDateTime.now(), 750895d);
			client3.addAccount(VIN005);
			accountRepository.save(VIN005);

			Account VIN006 = new Account("VIN006", LocalDateTime.now().minusMonths(5), -760000d);
			client3.addAccount(VIN006);
			accountRepository.save(VIN006);

			Account VIN007 = new Account("VIN007", LocalDateTime.now(), 1d);
			client4.addAccount(VIN007);
			accountRepository.save(VIN007);

			Account VIN008 = new Account("VIN008", LocalDateTime.now().minusYears(1), 0.50d);
			client4.addAccount(VIN008);
			accountRepository.save(VIN008);

			Transaction ASDA01= new Transaction(TransactionType.DEBIT, 1024.54, "My first debit transaction :D", LocalDateTime.now(), VIN001);
			if(ASDA01.getType().getName() == "Debit"){
				double amount = ASDA01.getAmount();
				double Finalamount = amount * (-1);
				ASDA01.setAmount(Finalamount);
			}
			VIN001.addTransaction(ASDA01);
			transactionRepository.save(ASDA01);

			Transaction ASDA02= new Transaction(TransactionType.CREDIT, 1024.54, "My first Credit transaction :D", LocalDateTime.now(), VIN001);
			if(ASDA02.getType().getName() == "Debit"){
				double amount = ASDA02.getAmount();
				double Finalamount = amount * (-1);
				ASDA02.setAmount(Finalamount);
			}
			VIN001.addTransaction(ASDA02);
			transactionRepository.save(ASDA02);

			Transaction ASDA03= new Transaction(TransactionType.DEBIT, 5678.54, "My first Debit transaction :D", LocalDateTime.now(), VIN002);
			if(ASDA03.getType().getName() == "Debit"){
				double amount = ASDA03.getAmount();
				double Finalamount = amount * (-1);
				ASDA03.setAmount(Finalamount);
			}
			VIN002.addTransaction(ASDA03);
			transactionRepository.save(ASDA03);

			Transaction ASDA04= new Transaction(TransactionType.CREDIT, 4868.54, "My first Credit transaction :D", LocalDateTime.now(), VIN003);
			if(ASDA04.getType().getName() == "Debit"){
				double amount = ASDA04.getAmount();
				double Finalamount = amount * (-1);
				ASDA04.setAmount(Finalamount);
			}
			VIN003.addTransaction(ASDA04);
			transactionRepository.save(ASDA04);

			List<Integer> payments1 = Arrays.asList(12,24,36,48,60);
			List<Integer> payments2 = Arrays.asList(6,12,24);
			List<Integer> payments3 = Arrays.asList(6,12,24,36);
			Loan mortgage = new Loan("Mortgage", 500000, payments1);
			Loan personal = new Loan("Personal", 100000, payments2);
			Loan automotive = new Loan("Automotive", 300000, payments3);
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			ClientLoan loan1 = new ClientLoan(36, 250000, LocalDateTime.now());
			mortgage.addClientLoan(loan1);
			client.addClientLoans(loan1);
			clientLoanRepository.save(loan1);

            ClientLoan loan2 = new ClientLoan(12, 789456, LocalDateTime.now());
			mortgage.addClientLoan(loan2);
            client.addClientLoans(loan2);
            clientLoanRepository.save(loan2);

            ClientLoan loan3 = new ClientLoan(12, 789456, LocalDateTime.now());
            automotive.addClientLoan(loan3);
            client.addClientLoans(loan3);
            clientLoanRepository.save(loan3);

			Card card1 = new Card(CardType.CREDIT, CardColor.GOLD, "2568 - 4784 - 9513 - 5432", 549, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client.addCard(card1);
			cardRepository.save(card1);

			Card card4 = new Card(CardType.CREDIT, CardColor.SILVER, "2568 - 4784 - 9513 - 5432", 549, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client.addCard(card4);
			cardRepository.save(card4);

			Card card2 = new Card(CardType.DEBIT, CardColor.TITANIUM, "4784 - 5432 - 7456 - 7193", 945, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client.addCard(card2);
			cardRepository.save(card2);

			Card card3 = new Card(CardType.DEBIT, CardColor.TITANIUM, "4784 - 7193 - 2568 - 5432", 594, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client2.addCard(card3);
			cardRepository.save(card3);

		};
	}
}
