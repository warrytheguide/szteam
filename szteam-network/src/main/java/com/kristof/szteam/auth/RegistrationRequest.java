package com.kristof.szteam.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "Felhasználónév megadása kötelező!")
    @NotBlank(message = "Felhasználónév megadása kötelező!")
    private String username;
    @Email(message = "Helytelen email!")
    @NotEmpty(message = "Email megadása kötelező!")
    @NotBlank(message = "Email megadása kötelező!")
    private String email;
    @NotEmpty(message = "Jelszó megadása kötelező!")
    @NotBlank(message = "Jelszó megadása kötelező!")
    @Size(min = 8, message = "A jelszónak legalább 8 karakter hosszúnak kell lennie!")
    private String password;
}
