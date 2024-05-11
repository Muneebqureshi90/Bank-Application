package com.example.bankingappbackend.service;

import com.example.bankingappbackend.utils.EmailDetails;

public interface EmailService {

    void sendEmail(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}

