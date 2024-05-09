package com.group.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserInfoDTO(
        @NotBlank(message = "Name must be not empty")
        @Size(min = 3, message = "Length userName must be min 3 char") String name,
        @Email(message = "This is not email")
        @NotBlank(message = "Email must be not empty") String email,
        String profilePictureUrl,
        Collection<? extends GrantedAuthority> roles

)

{
}
