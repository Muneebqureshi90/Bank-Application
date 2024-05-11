package com.example.bankingappbackend.repository;


import com.example.bankingappbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<RefreshToken,String> {


    Optional<RefreshToken> findByRefreshToken(String token);
}
