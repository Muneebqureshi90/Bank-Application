package com.example.bankingappbackend.service;

import com.example.bankingappbackend.dto.*;
import com.example.bankingappbackend.expections.UserException;

public interface UserService {

    BankResponce createBank(UserDto userDto);

    BankResponce balance(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest request);

    BankResponce creditAccount(CreditDebit creditDebit);

    BankResponce debitAccount(CreditDebit creditDebit);

    BankResponce transfer(Transfer transfer);
    UserDto registerUser(UserDto userDto);

    UserDto createUser(UserDto userDto) throws UserException;
}
