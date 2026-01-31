package com.openclassrooms.mddapi.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignupRequest;
import com.openclassrooms.mddapi.payload.response.AuthResponse;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.security.AuthTokens;
import com.openclassrooms.mddapi.service.IAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest request) {
        authService.register(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new MessageResponse("User registered successfully!"));
    }

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		AuthTokens tokens = authService.login(request);

		ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokens.getRefreshToken())
			.httpOnly(true)
			.path("/api/auth/refresh")
			.maxAge(Duration.ofMillis(tokens.getRefreshTokenExpiresIn()))
			.build();

		AuthResponse response = new AuthResponse(
			tokens.getAccessToken(),
			"Bearer",
			tokens.getAccessTokenExpiresIn() / 1000
		);

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
			.body(response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
		AuthTokens tokens = authService.refresh(refreshToken);

		ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokens.getRefreshToken())
			.httpOnly(true)
			.path("/api/auth/refresh")
			.maxAge(Duration.ofMillis(tokens.getRefreshTokenExpiresIn()))
			.build();

		AuthResponse response = new AuthResponse(
			tokens.getAccessToken(),
			"Bearer",
			tokens.getAccessTokenExpiresIn() / 1000
		);

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
			.body(response);
	}
}
