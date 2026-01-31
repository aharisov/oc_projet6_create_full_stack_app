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
import com.openclassrooms.mddapi.payload.response.AuthResponse;
import com.openclassrooms.mddapi.repository.UserRepository;
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
	public AuthResponse login(LoginRequest request) {
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
		return new AuthResponse(
			accessToken,
			"Bearer",
			jwtService.getAccessTokenExpirationMs() / 1000
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
