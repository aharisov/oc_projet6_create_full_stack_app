package com.openclassrooms.mddapi.payload.response;

import java.time.Instant;

public class UserProfileResponse {
	private Long id;
	private String email;
	private String username;
	private Instant createdAt;
	private Instant updatedAt;

	public UserProfileResponse(Long id, String email, String username, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() { return id; }
	public String getEmail() { return email; }
	public String getUsername() { return username; }
	public Instant getCreatedAt() { return createdAt; }
	public Instant getUpdatedAt() { return updatedAt; }
}
