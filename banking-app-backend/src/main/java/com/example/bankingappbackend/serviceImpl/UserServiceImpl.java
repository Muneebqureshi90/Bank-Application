package com.example.bankingappbackend.serviceImpl;

import com.example.bankingappbackend.dto.*;
import com.example.bankingappbackend.entity.User;
import com.example.bankingappbackend.expections.UserException;
import com.example.bankingappbackend.repository.UserRepository;
import com.example.bankingappbackend.service.EmailService;
import com.example.bankingappbackend.service.TransactionService;
import com.example.bankingappbackend.service.UserService;
import com.example.bankingappbackend.utils.AccountUtils;
import com.example.bankingappbackend.utils.EmailDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BankResponce createBank(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            BankResponce response = BankResponce.builder()
                    .responceCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responceMessage(AccountUtils.ACCOUNT_EXISTS_Message)
                    .accounInfo(null)
                    .build();
            return response;
        }
        // Encode the password using passwordEncoder
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        // Create an account -- save a new user in the database
        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(encodedPassword)
                .otherName(userDto.getOtherName())
                .gender(userDto.getGender())
                .address(userDto.getAddress())
                .stateOfOrigin(userDto.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userDto.getEmail())
                .alternativeNumber(userDto.getAlternativeNumber())
                .phoneNumber(userDto.getPhoneNumber())
                .status("ACTIVE")
                .build();

        // Save the new user to the database
        User saved = userRepository.save(newUser);

//        sending email
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saved.getEmail())
                .subject("Banking App Account Creation")
                .messageBody("Congratulation! Your Banking App Account Created Successfully. \n Your Account Details: \n" +
                        "Account Number:" + saved.getAccountNumber() + " " + "\n Account Holder Name:" + saved.getFirstName() + " " + saved.getLastName() + " " + saved.getOtherName())
                .build();
        emailService.sendEmail(emailDetails);

        // Return a success response
        return BankResponce.builder()
                .responceCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responceMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accounInfo(AccounInfo.builder()
                        .accountBalance(saved.getAccountBalance())
                        .accountNumber(saved.getAccountNumber())
                        .accountName(saved.getFirstName() + " " + saved.getLastName() + " " + saved.getOtherName())
                        .build())
                .build();
    }


//    balance Enquiry,name,credit,debit,transfer

    @Override
    public BankResponce balance(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponce.builder()
                    .responceCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responceMessage(AccountUtils.ACCOUNT_EXISTS_Message)
                    .accounInfo(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponce.builder()
                .responceCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responceMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accounInfo(AccounInfo.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());
        return user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
    }

    @Override
    public BankResponce creditAccount(CreditDebit creditDebit) {
        // checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebit.getAccountNumber());
        if (!isAccountExist) {
            return BankResponce.builder()
                    .responceCode(AccountUtils.ACCOUNT_NOT_EXIST_Code)
                    .responceMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accounInfo(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(creditDebit.getAccountNumber());
        user.setAccountBalance(user.getAccountBalance().add(creditDebit.getAmount()));
        userRepository.save(user);

//        save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(user.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditDebit.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponce.builder()
                .responceCode(AccountUtils.ACCOUNT_CREDIT_CODE)
                .responceMessage(AccountUtils.ACCOUNT_CREDIT_MESSAGE)
                .accounInfo(AccounInfo.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(creditDebit.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponce debitAccount(CreditDebit creditDebit) {
        // Checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebit.getAccountNumber());
        if (!isAccountExist) {
            return BankResponce.builder()
                    .responceCode(AccountUtils.ACCOUNT_NOT_EXIST_Code)
                    .responceMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accounInfo(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(creditDebit.getAccountNumber());
        BigDecimal amountToDebit = creditDebit.getAmount();
        BigDecimal currentBalance = user.getAccountBalance();
        // Checking if the account balance is sufficient for the debit transaction
        if (currentBalance.compareTo(amountToDebit) < 0) {
            return BankResponce.builder()
                    .responceCode("004")
                    .responceMessage("Insufficient Balance")
                    .accounInfo(null)
                    .build();
        }


        // Performing the debit transaction
        user.setAccountBalance(currentBalance.subtract(amountToDebit));
        userRepository.save(user);
        // Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(user.getAccountNumber())
                .transactionType("DEBIT")
                .amount(creditDebit.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);
        return BankResponce.builder()
                .responceCode(AccountUtils.ACCOUNT_DEBIT_CODE)
                .responceMessage(AccountUtils.ACCOUNT_DEBIT_MESSAGE)
                .accounInfo(AccounInfo.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(creditDebit.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponce transfer(Transfer transfer) {
        // Get the sender's account
        User sender = userRepository.findByAccountNumber(transfer.getSourceAccountNumber());

        // Check if the sender's account exists and has sufficient balance
        if (sender == null || sender.getAccountBalance().compareTo((transfer.getAmount())) < 0) {
            return BankResponce.builder()
                    .responceCode(AccountUtils.ACCOUNT_NOT_EXIST_Code)
                    .responceMessage("Sender account does not exist or insufficient balance")
                    .accounInfo(null)
                    .build();
        }

        // Debit the amount from the sender's account
        sender.setAccountBalance(sender.getAccountBalance().subtract((transfer.getAmount())));
        String sourceUserName = sender.getFirstName() + " " + sender.getLastName() + " " + sender.getOtherName();
        userRepository.save(sender);

        String messageBody = String.format("Dear %s,\n\n", sourceUserName);
        messageBody += String.format("A debit of %s has been made from your account.\n", transfer.getAmount());
        messageBody += String.format("Your current account balance is: %s\n\n", sender.getAccountBalance());
        messageBody += "Thank you for banking with us.";

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("Debit Alert")
                .recipient(sender.getEmail())
                .messageBody(messageBody)
                .build();

        emailService.sendEmail(debitAlert);

        // Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(sender.getAccountNumber())
                .transactionType("DEBIT")
                .amount(transfer.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        // Get the recipient's account
        User recipient = userRepository.findByAccountNumber(transfer.getDestinationAccountNumber());

        // Check if the recipient's account exists
        if (recipient == null) {
            return BankResponce.builder()
                    .responceCode(AccountUtils.ACCOUNT_NOT_EXIST_Code)
                    .responceMessage("Recipient account does not exist")
                    .accounInfo(null)
                    .build();
        }

        // Credit the amount to the recipient's account
        recipient.setAccountBalance(recipient.getAccountBalance().add((transfer.getAmount())));
        String recipientUserName = recipient.getFirstName() + " " + recipient.getLastName() + " " + recipient.getOtherName();
        userRepository.save(recipient);

        String messageBody1 = String.format("Dear %s,\n\n", recipientUserName);
        messageBody1 += String.format("You have received a credit of %s in your account.\n", transfer.getAmount());
        messageBody1 += String.format("Your current account balance is: %s\n\n", recipient.getAccountBalance());
        messageBody1 += "Thank you for banking with us.";

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("Credit Alert")
                .recipient(recipient.getEmail())
                .messageBody(messageBody1)
                .build();

        emailService.sendEmail(creditAlert);

        // Save transaction
        TransactionDto transactionDto1 = TransactionDto.builder()
                .accountNumber(recipient.getAccountNumber())
                .transactionType("CREDIT")
                .amount(transfer.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto1);

        // Return success response
        return BankResponce.builder()
                .responceCode(AccountUtils.ACCOUNT_TRANSFER_CODE)
                .responceMessage(AccountUtils.ACCOUNT_TRANSFER_MESSAGE)
                .accounInfo(null)
                .build();
    }


    @Override
    public UserDto registerUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = this.userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userRepository.save(user);
        return modelMapper.map(saveUser, UserDto.class);
    }


}
