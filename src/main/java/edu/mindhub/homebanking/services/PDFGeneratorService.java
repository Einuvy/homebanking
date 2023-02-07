package edu.mindhub.homebanking.services;

import com.itextpdf.text.DocumentException;
import edu.mindhub.homebanking.dto.AccountDTO;
import edu.mindhub.homebanking.dto.TransactionDTO;
import edu.mindhub.homebanking.models.Transaction;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


public interface PDFGeneratorService {

    public void generatePDF(HttpServletResponse response) throws IOException, DocumentException;

    public void addTitle(String text) throws DocumentException;

    public void addParagraph(String text) throws DocumentException;

    public void addSpace() throws DocumentException;

    public void addTransaction(Set<Transaction> transactions) throws DocumentException;

    public void addAccount(AccountDTO accountDTO) throws DocumentException;
}
