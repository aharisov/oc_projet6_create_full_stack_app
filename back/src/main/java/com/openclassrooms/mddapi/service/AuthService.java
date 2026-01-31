package com.openclassrooms.mddapi.service;

import org.modelmapper.ModelMapper;
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
import com.openclassrooms.mddapi.security.AuthTokens;
import com.openclassrooms.mddapi.security.JwtService;

@Service
public class AuthService implements IAuthService {
	private final UserRepository userRepository;
	private final ModelMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

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
			throw new ConflictException("Email already in use");
		}
		if (userRepository.existsByUsername(username)) {
			throw new ConflictException("Username already in use");
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());

		User userInfo = mapper.map(request, User.class);
		userInfo.setEmail(email);
		userInfo.setUsername(username);
		userInfo.setPasswordHash(encodedPassword);

        userRepository.save(userInfo);

		return true;
    }

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
			throw new UnauthorizedException("Invalid credentials");
		}

		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		return new AuthTokens(
			accessToken,
			refreshToken,
			jwtService.getAccessTokenExpirationMs(),
			jwtService.getRefreshTokenExpirationMs()
		);
	}

	@Override
	public AuthTokens refresh(String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new BadRequestException("Refresh token is required");
		}
		if (!jwtService.isTokenValid(refreshToken)) {
			throw new UnauthorizedException("Invalid refresh token");
		}
		String tokenType = jwtService.getTokenType(refreshToken);
		if (!"refresh".equals(tokenType)) {
			throw new UnauthorizedException("Invalid refresh token");
		}
		String subject = jwtService.getSubject(refreshToken);
		Long userId = Long.valueOf(subject);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

		String accessToken = jwtService.generateAccessToken(user);
		String newRefreshToken = jwtService.generateRefreshToken(user);
		return new AuthTokens(
			accessToken,
			newRefreshToken,
			jwtService.getAccessTokenExpirationMs(),
			jwtService.getRefreshTokenExpirationMs()
		);
	}

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
}
