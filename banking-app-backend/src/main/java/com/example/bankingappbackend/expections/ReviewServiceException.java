package com.example.bankingappbackend.expections;

public class ReviewServiceException extends Exception {

    public ReviewServiceException(String message, Throwable cause){
        super(message, cause);
    }
}
