package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
	@Email(message = "Email is invalid")
	@Size(max = 50, message = "Max email length is 50 characters.")
	private String email;

	@Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters.")
	private String username;

	@Size(min = 8, message = "Min password length is 8 characters.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!?.*()_\\-]).+$",
		message = "The password must contain lowercase letters, uppercase letters, numbers, and special characters."
	)
	private String password;

	public UpdateUserRequest() {}

	public UpdateUserRequest(String email, String username, String password) {
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
