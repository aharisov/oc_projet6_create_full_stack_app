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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Exposes profile endpoints for the currently authenticated user.
 */
@RestController
@RequestMapping("/users/me")
@Tag(name = "Users", description = "Current user profile endpoints")
public class UserController {

	private final IUserService userService;

	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@Operation(summary = "Get profile", description = "Returns current authenticated user profile")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Profile returned"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "User not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public UserProfileResponse getUserProfile() {
		
		return userService.getUserProfile();
	}

	@PutMapping
	@Operation(summary = "Update profile", description = "Updates current authenticated user profile")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Profile updated"),
		@ApiResponse(responseCode = "400", description = "Invalid request data",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "User not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "409", description = "Email or username already exists",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
		userService.updateUser(request);

		return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
	}
}
