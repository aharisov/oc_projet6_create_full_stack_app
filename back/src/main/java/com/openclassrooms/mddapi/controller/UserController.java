package com.openclassrooms.mddapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.payload.response.UserProfileResponse;
import com.openclassrooms.mddapi.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/me")
public class UserController {

	private final IUserService userService;

	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public UserProfileResponse getUserProfile() {
		
		return userService.getUserProfile();
	}

	@PutMapping
	public ResponseEntity<MessageResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
		userService.updateUser(request);

		return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
	}
}
