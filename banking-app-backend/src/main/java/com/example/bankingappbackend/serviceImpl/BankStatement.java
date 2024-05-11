package com.example.bankingappbackend.serviceImpl;

import com.example.bankingappbackend.entity.Transaction;
import com.example.bankingappbackend.entity.User;
import com.example.bankingappbackend.repository.TransactionRepository;
import com.example.bankingappbackend.repository.UserRepository;
import com.example.bankingappbackend.service.EmailService;
import com.example.bankingappbackend.utils.EmailDetails;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    private TransactionRepository transactionRepository;

    private UserRepository userRepository;

    private EmailService emailService;

    private static final String FILE_PATH = "C:\\Users\\uni\\Documents\\bankStatement_springboot.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {


        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> {
                    if (transaction.getCreatedAt() != null && transaction.getModifiedAt() != null) {
                        LocalDate transactionDate = transaction.getCreatedAt().toLocalDate();
                        return transactionDate.isEqual(start) || transactionDate.isEqual(end);
                    }
                    return false;
                })
                .collect(Collectors.toList());


        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();

//        Desgin the Table of Bank Statement
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of page " + statementSize);
        OutputStream outputStream = new FileOutputStream(FILE_PATH);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Bank Info"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("72,Some Address,Lagos Nigeria"));
        bankAddress.setBorder(0);

        // Add cells to the table
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementTable = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date:" + startDate));

        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);

        PdfPCell stopDate = new PdfPCell(new Phrase("End DATE" + endDate));
        stopDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("CUSTOMER NAME" + customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();
        PdfPCell address = new PdfPCell(new Phrase("Customer Address" + user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        transactions.forEach(transaction -> {
            transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementTable.addCell(customerInfo);
        statementTable.addCell(statement);
        statementTable.addCell(endDate);
        statementTable.addCell(name);
        statementTable.addCell(space);
        statementTable.addCell(address);

        document.add(bankInfoTable);
        document.add(statementTable);
        document.add(transactionTable);
        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your request amount statement attached!")
                .attachment(FILE_PATH)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactions;

    }
}