// In: com.greenride.dto.RegisterRequest.java
package com.greenride.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Using "record" is a modern, concise way to create DTOs
public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 20) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 40) String password
) {}