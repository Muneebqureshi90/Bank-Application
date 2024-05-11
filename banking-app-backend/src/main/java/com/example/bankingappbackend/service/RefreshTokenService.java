package com.example.bankingappbackend.service;


import com.example.bankingappbackend.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String userName);
    RefreshToken verifyRefreshToken(String refreshToken);

}
