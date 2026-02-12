package com.openclassrooms.mddapi.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.ConflictException;
import com.openclassrooms.mddapi.exception.UnauthorizedException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.payload.response.UserProfileResponse;
import com.openclassrooms.mddapi.repository.UserRepository;

@Service
public class UserService implements IUserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			log.warn("Unauthorized access attempt!");
			throw new UnauthorizedException("Unauthorized");
		}
		String email = String.valueOf(auth.getPrincipal());
		return userRepository.findByEmail(email)
			.orElseThrow(() -> {
				log.warn("Unauthorized access attempt!");
				return new UnauthorizedException("Unauthorized");
			});
	}

	@Override
	public UserProfileResponse getUserProfile() {
		User user = this.getCurrentUser();
		
		return new UserProfileResponse(
			user.getId(),
			user.getEmail(),
			user.getUsername(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);
	}

	@Override
	@Transactional
	public void updateUser(UpdateUserRequest request) {
		if (request == null) {
			throw new BadRequestException("Update payload is required");
		}

		User user = getCurrentUser();
		boolean updated = updateEmail(user, request.getEmail());
		updated = updateUsername(user, request.getUsername()) || updated;
		updated = updatePassword(user, request.getPassword()) || updated;

		if (!updated) {
			throw new BadRequestException("No changes detected");
		}

		userRepository.save(Objects.requireNonNull(user));
		log.info("User updated: id={}", user.getId());
	}

	private boolean updateEmail(User user, String rawEmail) {
		if (rawEmail == null) {
			return false;
		}

		String email = rawEmail.trim().toLowerCase();
		if (email.isBlank()) {
			throw new BadRequestException("Email cannot be blank");
		}
		if (email.equals(user.getEmail())) {
			return false;
		}
		if (userRepository.existsByEmailAndIdNot(email, user.getId())) {
			log.warn("User update blocked: email already in use");
			throw new ConflictException("Email already in use");
		}

		user.setEmail(email);
		return true;
	}

	private boolean updateUsername(User user, String rawUsername) {
		if (rawUsername == null) {
			return false;
		}

		String username = rawUsername.trim();
		if (username.isBlank()) {
			throw new BadRequestException("Username cannot be blank");
		}
		if (username.equals(user.getUsername())) {
			return false;
		}
		if (userRepository.existsByUsernameAndIdNot(username, user.getId())) {
			log.warn("User update blocked: username already in use");
			throw new ConflictException("Username already in use");
		}

		user.setUsername(username);
		return true;
	}

	private boolean updatePassword(User user, String rawPassword) {
		if (rawPassword == null) {
			return false;
		}
		if (rawPassword.isBlank()) {
			throw new BadRequestException("Password cannot be blank");
		}
		if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
			return false;
		}

		user.setPasswordHash(passwordEncoder.encode(rawPassword));
		return true;
	}
}
