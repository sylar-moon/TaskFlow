package com.group.controller;

import com.group.dto.UserInfoDTO;
import com.group.security.CustomUserDetails;
import com.group.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    PersonService personService;

    @Autowired
    public UserController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping
    public ResponseEntity<UserInfoDTO> getMeInfo(Principal principal){
        CustomUserDetails userDetails = personService.loadUserByUsername(principal.getName());
        return ResponseEntity.ok(new UserInfoDTO(userDetails.getUsername(),
                userDetails.getMail(),
                userDetails.getUserPic(),
                userDetails.getAuthorities()
                ));
    }
}
