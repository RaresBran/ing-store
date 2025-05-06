package com.ing.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotEmpty( message = "Email is mandatory")
    @NotBlank( message = "Email is mandatory")
    @Email
    private String email;

    @NotEmpty( message = "Password is mandatory")
    @NotBlank( message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    String password;
}
