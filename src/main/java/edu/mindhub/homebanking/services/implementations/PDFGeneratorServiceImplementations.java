package edu.mindhub.homebanking.services.implementations;
import edu.mindhub.homebanking.dto.AccountDTO;
import edu.mindhub.homebanking.dto.TransactionDTO;
import edu.mindhub.homebanking.models.Transaction;
import edu.mindhub.homebanking.services.PDFGeneratorService;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

@Service
public class PDFGeneratorServiceImplementations implements PDFGeneratorService {
    Document document = new Document(PageSize.A4);
    @Override
    public void generatePDF(HttpServletResponse response) throws IOException, DocumentException {
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

    }

    @Override
    public void addTitle(String text) throws DocumentException{
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.TIMES)));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        document.add(table);
    }

    @Override
    public void addParagraph(String text) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(text, FontFactory.getFont(FontFactory.TIMES)));
        document.add(paragraph);
    }

    @Override
    public void addSpace() throws DocumentException{
        Paragraph lineJumps = new Paragraph();
        lineJumps.add(new Phrase(Chunk.NEWLINE));
        lineJumps.add(new Phrase(Chunk.NEWLINE));
        document.add(lineJumps);
    }

    @Override
    public void addTransaction(Set<Transaction> transactions) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.addCell("Description");
        table.addCell("Amount");
        table.addCell("Type");
        table.addCell("Date");
        transactions.forEach(transaction -> {
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getAmount() + "");
            table.addCell(transaction.getType() + "");
            table.addCell(transaction.getDate() + "");

        });

        try {
            document.add(table);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAccount(AccountDTO accountDTO) throws DocumentException{
        PdfPTable table = new PdfPTable(5);
        table.addCell("Id");
        table.addCell("Number");
        table.addCell("Balance");
        table.addCell("Type");
        table.addCell("Creation Date");

        table.addCell(accountDTO.getId() + "");
        table.addCell(accountDTO.getNumber());
        table.addCell(accountDTO.getBalance() + "");
        table.addCell( "");
        table.addCell(accountDTO.getCreationDate() + "");

        document.add(table);
    }

    public void closeDocument() {
        document.close();
    }
}
