package com.openclassrooms.mddapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.exception.UnauthorizedException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

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
}
