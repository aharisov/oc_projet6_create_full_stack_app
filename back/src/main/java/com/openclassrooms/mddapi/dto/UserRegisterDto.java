package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class UserRegisterDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Size(max = 50, message = "Max email length is 50 characters.")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username length must be between 3 and 30 characters.")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Min password length is 8 characters.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
        message = "The password must contain lowercase letters, uppercase letters, numbers, and special characters."
    )
    private String password;

    public UserRegisterDto(String email, String username, String password) {
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
