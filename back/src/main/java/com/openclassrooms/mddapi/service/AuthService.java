package com.openclassrooms.mddapi.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.ConflictException;
import com.openclassrooms.mddapi.exception.UnauthorizedException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignupRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.model.RefreshToken;
import com.openclassrooms.mddapi.repository.RefreshTokenRepository;
import com.openclassrooms.mddapi.security.AuthTokens;
import com.openclassrooms.mddapi.security.JwtService;

/**
 * Implements authentication workflows and refresh-token lifecycle management.
 *
 * <p>Refresh tokens are stored as SHA-256 hashes to avoid persisting raw secrets in
 * the database.</p>
 */
@Service
public class AuthService implements IAuthService {
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final ModelMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	/**
	 * Registers a user after validating required fields and uniqueness constraints.
	 *
	 * @param request signup payload
	 * @return {@code true} when registration succeeds
	 * @throws BadRequestException when required fields are missing
	 * @throws ConflictException when email or username is already used
	 */
	@Override
	@Transactional
	public Boolean register(SignupRequest request) {
		String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null;
		String username = request.getUsername() != null ? request.getUsername().trim() : null;

		if (email == null || email.isBlank()) {
			throw new BadRequestException("Email is required");
		}
		if (username == null || username.isBlank()) {
			throw new BadRequestException("Username is required");
		}
		if (request.getPassword() == null || request.getPassword().isBlank()) {
			throw new BadRequestException("Password is required");
		}

		if (userRepository.existsByEmail(email)) {
			log.warn("Registration blocked: email already in use");
			throw new ConflictException("Email already in use");
		}
		if (userRepository.existsByUsername(username)) {
			log.warn("Registration blocked: username already in use");
			throw new ConflictException("Username already in use");
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());

		User userInfo = mapper.map(request, User.class);
		userInfo.setEmail(email);
		userInfo.setUsername(username);
		userInfo.setPasswordHash(encodedPassword);

        userRepository.save(userInfo);
		log.info("User registered: id={}", userInfo.getId());

		return true;
    }

	/**
	 * Authenticates a user with email or username and issues token pair.
	 *
	 * @param request login payload
	 * @return new access and refresh tokens
	 * @throws BadRequestException when credentials are missing
	 * @throws UnauthorizedException when credentials are invalid
	 */
	@Override
	public AuthTokens login(LoginRequest request) {
		String identifier = request.getIdentifier() != null ? request.getIdentifier().trim() : null;
		String password = request.getPassword();

		if (identifier == null || identifier.isBlank()) {
			throw new BadRequestException("Identifier is required");
		}
		if (password == null || password.isBlank()) {
			throw new BadRequestException("Password is required");
		}

		User user = resolveUser(identifier);
		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			log.warn("Login failed: invalid credentials");
			throw new UnauthorizedException("Invalid credentials");
		}

		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		saveRefreshToken(user, refreshToken);
		log.info("Login success: userId={}", user.getId());
		return new AuthTokens(
			accessToken,
			refreshToken,
			jwtService.getAccessTokenExpirationMs(),
			jwtService.getRefreshTokenExpirationMs()
		);
	}

	/**
	 * Rotates the refresh token and returns a new token pair.
	 *
	 * @param refreshToken refresh token sent by the client
	 * @return renewed access and refresh tokens
	 * @throws BadRequestException when token is missing
	 * @throws UnauthorizedException when token is invalid, expired, or mismatched
	 */
	@Override
	public AuthTokens refresh(String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new BadRequestException("Refresh token is required");
		}
		if (!jwtService.isTokenValid(refreshToken)) {
			log.warn("Refresh failed: token invalid");
			throw new UnauthorizedException("Invalid refresh token");
		}
		String tokenType = jwtService.getTokenType(refreshToken);
		if (!"refresh".equals(tokenType)) {
			log.warn("Refresh failed: wrong token type");
			throw new UnauthorizedException("Invalid refresh token");
		}
		String subject = jwtService.getSubject(refreshToken);
		Long userId = Long.valueOf(subject);

		RefreshToken storedRefreshToken = refreshTokenRepository.findByUserId(userId)
			.orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
		
		if (storedRefreshToken.getExpiresAt().isBefore(Instant.now())) {
			log.warn("Refresh failed: token expired");
			throw new UnauthorizedException("Invalid refresh token");
		}
		
		if (!hashToken(refreshToken).equals(storedRefreshToken.getTokenHash())) {
			log.warn("Refresh failed: token hash mismatch");
			throw new UnauthorizedException("Invalid refresh token");
		}

		User user = storedRefreshToken.getUser();

		String accessToken = jwtService.generateAccessToken(user);
		String newRefreshToken = jwtService.generateRefreshToken(user);
		saveRefreshToken(user, newRefreshToken);
		log.info("Refresh success: userId={}", user.getId());
		return new AuthTokens(
			accessToken,
			newRefreshToken,
			jwtService.getAccessTokenExpirationMs(),
			jwtService.getRefreshTokenExpirationMs()
		);
	}

	/**
	 * Invalidates an existing refresh token for logout.
	 *
	 * @param refreshToken refresh token sent by the client
	 * @throws BadRequestException when token is missing
	 * @throws UnauthorizedException when token is invalid, expired, or mismatched
	 */
	@Override
	public void logout(String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new BadRequestException("Refresh token is required");
		}
		if (!jwtService.isTokenValid(refreshToken)) {
			log.warn("Logout failed: token invalid");
			throw new UnauthorizedException("Invalid refresh token");
		}
		String tokenType = jwtService.getTokenType(refreshToken);
		if (!"refresh".equals(tokenType)) {
			log.warn("Logout failed: wrong token type");
			throw new UnauthorizedException("Invalid refresh token");
		}
		String subject = jwtService.getSubject(refreshToken);
		Long userId = Long.valueOf(subject);

		RefreshToken storedRefreshToken = refreshTokenRepository.findByUserId(userId)
			.orElseThrow(() -> {
				log.warn("Logout failed: refresh token not found");
				return new UnauthorizedException("Invalid refresh token");
			});

		if (storedRefreshToken.getExpiresAt().isBefore(Instant.now())) {
			log.warn("Logout failed: token expired");
			throw new UnauthorizedException("Invalid refresh token");
		}

		if (!hashToken(refreshToken).equals(storedRefreshToken.getTokenHash())) {
			log.warn("Logout failed: token hash mismatch");
			throw new UnauthorizedException("Invalid refresh token");
		}

		refreshTokenRepository.delete(storedRefreshToken);
		log.info("Logout success: userId={}", userId);
	}

	/**
	 * Resolves a user by identifier (username or email).
	 *
	 * @param identifier login identifier
	 * @return resolved user
	 * @throws UnauthorizedException when no matching user exists
	 */
	private User resolveUser(String identifier) {
		String normalized = identifier.toLowerCase();
		if (normalized.contains("@")) {
			return userRepository.findByEmail(normalized)
				.orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
		}
		return userRepository.findByUsername(identifier)
			.orElseGet(() -> userRepository.findByEmail(normalized)
				.orElseThrow(() -> new UnauthorizedException("Invalid credentials")));
	}

	/**
	 * Stores or updates the hashed refresh token for a user.
	 *
	 * @param user token owner
	 * @param refreshToken raw refresh token
	 */
	private void saveRefreshToken(User user, String refreshToken) {
		RefreshToken storedRefreshToken = refreshTokenRepository.findByUserId(user.getId())
			.orElseGet(RefreshToken::new);
		storedRefreshToken.setUser(user);
		storedRefreshToken.setTokenHash(hashToken(refreshToken));
		storedRefreshToken.setExpiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()));
		
		refreshTokenRepository.save(storedRefreshToken);
	}

	/**
	 * Produces a stable SHA-256 hash for token persistence and comparison.
	 *
	 * @param token raw token value
	 * @return hexadecimal SHA-256 hash
	 */
	private String hashToken(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
			StringBuilder hex = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
				hex.append(String.format("%02x", b));
			}
			return hex.toString();
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("SHA-256 not available", ex);
		}
	}
}
