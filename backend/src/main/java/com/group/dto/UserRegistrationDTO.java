package com.group.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
        @NotBlank(message = "Name must be not empty")
        @Size(min = 3, message = "Length userName must be min 3 char") String userName,
        @Email(message = "This is not email")
        @NotBlank(message = "Email must be not empty") String email,
        @NotBlank(message = "Password must be not empty")
        @Size(min = 4, message = "Length password must be min 4 char") String password) {
}
