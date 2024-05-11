package com.example.bankingappbackend.serviceImpl;

import com.example.bankingappbackend.service.EmailService;
import com.example.bankingappbackend.utils.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(senderEmail);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getMessageBody(), true); // Use true to enable HTML content
            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

    @Override
    public void sendEmailWithAttachment(EmailDetails emailDetails) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true); // true indicates multipart message
            helper.setFrom(senderEmail);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getMessageBody());

            // Attach the file
            FileSystemResource attachmentFile = new FileSystemResource(emailDetails.getAttachment());
            helper.addAttachment(attachmentFile.getFilename(), attachmentFile);

            mailSender.send(mimeMessage);
            log.info(attachmentFile.getFilename() + " has been sent successfully to your email"
                    + emailDetails.getRecipient());
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception properly, e.g., log it
        }
    }
}
