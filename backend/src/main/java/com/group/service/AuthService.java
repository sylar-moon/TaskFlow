package com.group.service;

import com.group.dto.TokenDTO;
import com.group.dto.UserAuthDTO;
import com.group.dto.UserRegistrationDTO;
import com.group.util.JwtTokenUtil;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;

    private final PersonService personService;

    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       PersonService personService,
                       JwtTokenUtil jwtTokenUtil,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.personService = personService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }


    public void authenticateUser(UserAuthDTO authDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.password()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("user password and username is not correct", e);

        }
    }

    public void validateUserRegistration(UserRegistrationDTO dto) {
        if (personService.isNewPerson(dto.email())) {
            throw new ValidationException("User with the same username is already exists");
        }
    }


    public void registrationNewPerson(UserRegistrationDTO dto) {
        personService.saveNewPerson(new UserRegistrationDTO(dto.userName(),
                dto.email(), passwordEncoder.encode(dto.password()),null));
    }

    public TokenDTO getToken(String email) {
        return new TokenDTO(jwtTokenUtil.generateToken(personService.loadUserByUsername(email)));
    }
}
