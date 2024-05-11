package com.example.bankingappbackend.utils;

import java.time.Year;

public class AccountUtils {


    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_Message="This account already exists";
    public static final String ACCOUNT_CREATION_CODE="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Account is Created Successfully";
    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_MESSAGE="Account is Found Successfully";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="Account Not Exists";
    public static final String ACCOUNT_NOT_EXIST_Code="003";
    public static final String ACCOUNT_CREDIT_CODE="005";
    public static final String ACCOUNT_CREDIT_MESSAGE="User Account Added";
    public static final String ACCOUNT_DEBIT_MESSAGE = "Amount deducted from your account";
    public static final String ACCOUNT_DEBIT_CODE = "006";
    public static final String ACCOUNT_TRANSFER_CODE = "007";
    public static final String ACCOUNT_TRANSFER_MESSAGE = "Transfer successful";
    /**
     * Generates a unique account number based on the current year and a random number.
     *
     * @return The generated account number.
     */
    public static String generateAccountNumber() {
        // Get the current year
        Year currentYear = Year.now();

        // Define the minimum and maximum range for the random number
        int min = 100000;
        int max = 999999;

        // Generate a random number between min and max (inclusive)
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // Convert the current year and random number to strings
        String year = String.valueOf(currentYear.getValue());
        String randomNumber = String.valueOf(randNumber);

        // Build the account number by concatenating the year and random number
        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(year).append(randomNumber);

        // Return the generated account number as a String
        return accountNumber.toString();
    }
}
