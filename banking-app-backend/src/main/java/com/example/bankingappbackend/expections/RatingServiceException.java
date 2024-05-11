package com.example.bankingappbackend.expections;

public class RatingServiceException extends Exception {

    public RatingServiceException(String message, Throwable cause){
        super(message, cause);
    }
}
