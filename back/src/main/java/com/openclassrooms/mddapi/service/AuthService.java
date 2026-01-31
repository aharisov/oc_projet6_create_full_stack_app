package com.openclassrooms.mddapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.UserRegisterDto;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.ConflictException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;

@Service
public class AuthService implements IAuthService {
	private final UserRepository userRepository;
	private final ModelMapper mapper;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
    @Transactional
    public Boolean register(UserRegisterDto user) {
		String email = user.getEmail() != null ? user.getEmail().trim().toLowerCase() : null;
		String username = user.getUsername() != null ? user.getUsername().trim() : null;

		if (email == null || email.isBlank()) {
			throw new BadRequestException("Email is required");
		}
		if (username == null || username.isBlank()) {
			throw new BadRequestException("Username is required");
		}
		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new BadRequestException("Password is required");
		}

		if (userRepository.existsByEmail(email)) {
			throw new ConflictException("Email already in use");
		}
		if (userRepository.existsByUsername(username)) {
			throw new ConflictException("Username already in use");
		}

		String encodedPassword = passwordEncoder.encode(user.getPassword());

		User userInfo = mapper.map(user, User.class);
		userInfo.setEmail(email);
		userInfo.setUsername(username);
		userInfo.setPasswordHash(encodedPassword);

        userRepository.save(userInfo);

		return true;
    }
}
