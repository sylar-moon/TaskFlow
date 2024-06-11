package com.group.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record SendMessageDTO(@NotBlank
                             @Min(5)
                             @Email
                             String address,

                             @NotBlank
                             @Min(5)
                             String message,

                             @NotBlank
                             @Min(5)
                             String header) {
}
