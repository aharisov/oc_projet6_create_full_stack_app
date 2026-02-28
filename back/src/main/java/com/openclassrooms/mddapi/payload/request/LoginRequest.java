package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank(message = "L'identifiant est requis.")
	private String identifier;

	@NotBlank(message = "Le mot de passe est requis.")
	private String password;

	public LoginRequest() {}

	public LoginRequest(String identifier, String password) {
		this.identifier = identifier;
		this.password = password;
	}

	public String getIdentifier() { return identifier; }
	public void setIdentifier(String identifier) { this.identifier = identifier; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
}
