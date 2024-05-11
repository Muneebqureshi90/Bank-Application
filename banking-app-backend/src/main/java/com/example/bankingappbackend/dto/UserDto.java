package com.example.bankingappbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to create a new user account")
public class UserDto {
    @Schema(description = "The first name of the user")
    private String firstName;

    @Schema(description = "The last name of the user")
    private String lastName;

    @Schema(description = "The other name of the user")
    private String otherName;

    @Schema(description = "The gender of the user")
    private String gender;

    @Schema(description = "The address of the user")
    private String address;

    @Schema(description = "The state of origin of the user")
    private String stateOfOrigin;

    @Schema(description = "The email of the user")
    private String email;

    @Schema(description = "The phone number of the user")
    private String phoneNumber;

    @Schema(description = "An alternative phone number of the user")
    private String alternativeNumber;
    private String password;

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

}
