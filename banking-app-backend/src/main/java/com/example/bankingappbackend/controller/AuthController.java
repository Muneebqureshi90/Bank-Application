package com.example.bankingappbackend.controller;


import com.example.bankingappbackend.dto.UserDto;
import com.example.bankingappbackend.entity.RefreshToken;
import com.example.bankingappbackend.entity.User;
import com.example.bankingappbackend.security.JwtAuthRequest;
import com.example.bankingappbackend.security.JwtAuthResponse;
import com.example.bankingappbackend.security.JwtTokenHelper;
import com.example.bankingappbackend.security.RefreshTokenRequest;
import com.example.bankingappbackend.service.RefreshTokenService;
import com.example.bankingappbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "scheme1")
@RestController
@RequestMapping("/api/v1/auth/")
@Tag(name = "Authication Controller", description = "This is Authication Controller")

@CrossOrigin(origins = "*")

public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenHelper helper;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody JwtAuthRequest request) {

        this.doAuthenticate( request.getUserName(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
        String token = this.helper.generateToken(userDetails);

//        This is for Refresh Token
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());

//        JwtAuthResponse response = JwtAuthResponse
//                .responce(token)
//                .username(userDetails.getUsername())
//                .build();

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken.getRefreshToken());
        response.setUser(this.modelMapper.map((User)userDetails, UserDto.class));


        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            this.authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password!!");
        }
    }


    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }



    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid  @RequestBody UserDto userDto){
        UserDto userRegister= this.userService.registerUser(userDto);
        return new ResponseEntity<UserDto>(userRegister, HttpStatus.CREATED);
    }





//    Refresh Token
@PostMapping("/refresh")
public ResponseEntity<JwtAuthResponse> refreshJwtToken(@RequestBody RefreshTokenRequest request) {
    try {
        // Verify the refresh token and retrieve the associated user
        RefreshToken refreshToken = this.refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();

        // Generate a new JWT token for the user
        String newToken = this.helper.generateToken(user);

        // Create a response containing the new JWT token
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(newToken);

        // Return the response to the client
        return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
        // Handle exceptions thrown during token refresh, e.g., token expired or invalid
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
}



