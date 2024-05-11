package com.example.bankingappbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@Builder
@Schema(description = "User Entity")

@ToString(exclude = {"accountBalance", "status", "createdAT", "modifiedAT"}) // Exclude these fields from toString
@JsonIgnoreProperties({"hibernateLazyInitializer", "id"}) // Exclude these fields from JSON serialization
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "other_name")
    private String otherName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address; // Corrected typo here

    @Column(name = "state_of_origin") // Changed column name to follow Java naming conventions
    private String stateOfOrigin; // Corrected typo here

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "alternative_number") // Changed column name to follow Java naming conventions
    private String alternativeNumber; // Corrected typo here

    @Column(name = "status")
    private String status;

    @Column(name = "created_at") // Changed column name to follow Java naming conventions
    @CreationTimestamp
    private LocalDateTime createdAt; // Changed variable name to follow Java naming conventions

    @Column(name = "modified_at") // Changed column name to follow Java naming conventions
    @UpdateTimestamp
    private LocalDateTime modifiedAt; // Changed variable name to follow Java naming conventions
    //     Define a bidirectional relationship with RefreshToken
    @OneToOne
            (mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RefreshToken refreshToken;
    @Builder
    public User(Long id, String firstName, String lastName, String otherName, String gender,
                String address, String stateOfOrigin, String accountNumber, BigDecimal accountBalance,
                String email,String password, String phoneNumber, String alternativeNumber, String status,
                LocalDateTime createdAt, LocalDateTime modifiedAt,RefreshToken refreshToken) {
        this.id = id;
        this.firstName = firstName;
        this.refreshToken = refreshToken;

        this.lastName = lastName;
        this.otherName = otherName;
        this.gender = gender;
        this.address = address;
        this.stateOfOrigin = stateOfOrigin;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.alternativeNumber = alternativeNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        List<SimpleGrantedAuthority> authorities = this.roles.stream().map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
//
//        return authorities;

        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    // Exclude refreshToken from serialization
}
