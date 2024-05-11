package com.example.bankingappbackend.security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtAuthRequest {

    private String userName;
    private String password;

}
