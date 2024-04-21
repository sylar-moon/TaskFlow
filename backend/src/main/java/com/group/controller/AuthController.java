package com.group.controller;

import com.group.dto.TokenDTO;
import com.group.dto.UserAuthDTO;
import com.group.dto.UserRegistrationDTO;
import com.group.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenDTO> getAuthToken(@RequestBody UserAuthDTO dto) {
        authService.authenticateUser(dto);
        return ResponseEntity.ok(authService.getToken(dto.email()));
    }

    @PostMapping("/registration")
    public ResponseEntity<TokenDTO> registration(@RequestBody UserRegistrationDTO dto) {
        log.info("your registration data : {} {} {}",dto.email(), dto.userName(),dto.password());
        authService.validateUserRegistration(dto);
        authService.registrationNewPerson(dto);
        return ResponseEntity.ok(authService.getToken(dto.email()));
    }
}