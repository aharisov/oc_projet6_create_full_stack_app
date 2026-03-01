package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class SignupRequest {
    @NotBlank(message = "L'email est requis.")
    @Email(message = "L'email est invalide.")
    @Size(max = 50, message = "L'email ne peut pas dépasser 50 caractères.")
    private String email;

    @NotBlank(message = "Le nom d'utilisateur est requis.")
    @Size(min = 3, max = 30, message = "Le nom d'utilisateur doit contenir entre 3 et 30 caractères.")
    private String username;
    
    @NotBlank(message = "Le mot de passe est requis.")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
        message = "Le mot de passe doit contenir une minuscule, une majuscule, un chiffre et un caractère spécial."
    )
    private String password;

    public SignupRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
