package com.openclassrooms.mddapi.controller;

import java.time.Duration;
import java.util.Objects;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Exposes authentication endpoints for registration, login, token refresh, and logout.
 *
 * <p>Refresh tokens are exchanged through an HTTP-only cookie, while access tokens are
 * returned in the response body.</p>
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Register, login, refresh and logout endpoints")
public class AuthController {
    
    private final IAuthService authService;
	private static final String COOKIE_PATH = "/api/auth";
	private static final String COOKIE_NAME = "refresh_token";

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
	@Operation(summary = "Register user", description = "Creates a new user account")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "User registered"),
		@ApiResponse(responseCode = "400", description = "Invalid request data",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "409", description = "Email or username already exists",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest request) {
        authService.register(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new MessageResponse("User registered successfully!"));
    }

	@PostMapping("/login")
	@Operation(summary = "Login user", description = "Authenticates user and returns access token with refresh cookie")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Login successful"),
		@ApiResponse(responseCode = "400", description = "Invalid request data",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "401", description = "Invalid credentials",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		AuthTokens tokens = authService.login(request);

		ResponseCookie refreshCookie = ResponseCookie.from(COOKIE_NAME, tokens.getRefreshToken())
			.httpOnly(true)
			.path(COOKIE_PATH)
			.maxAge(Objects.requireNonNull(Duration.ofMillis(tokens.getRefreshTokenExpiresIn())))
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
	@Operation(summary = "Refresh access token", description = "Issues a new access token using refresh token cookie")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Token refreshed"),
		@ApiResponse(responseCode = "400", description = "Missing refresh token",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "401", description = "Invalid or expired refresh token",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<AuthResponse> refresh(@CookieValue(name = COOKIE_NAME, required = false) String refreshToken) {
		AuthTokens tokens = authService.refresh(refreshToken);

		ResponseCookie refreshCookie = ResponseCookie.from(COOKIE_NAME, tokens.getRefreshToken())
			.httpOnly(true)
			.path(COOKIE_PATH)
			.maxAge(Objects.requireNonNull(Duration.ofMillis(tokens.getRefreshTokenExpiresIn())))
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

	@PostMapping("/logout")
	@Operation(summary = "Logout user", description = "Invalidates refresh token and clears refresh cookie")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Logout successful"),
		@ApiResponse(responseCode = "400", description = "Missing refresh token",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "401", description = "Invalid or expired refresh token",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> logout(@CookieValue(name = COOKIE_NAME, required = false) String refreshToken) {
		authService.logout(refreshToken);

		ResponseCookie refreshCookie = ResponseCookie.from(COOKIE_NAME, "")
			.httpOnly(true)
			.path(COOKIE_PATH)
			.maxAge(0)
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
			.body(new MessageResponse("Logged out"));
	}
}
