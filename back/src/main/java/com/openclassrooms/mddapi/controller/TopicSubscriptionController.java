package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.ISubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Handles subscription operations between the current user and topics.
 */
@RestController
@RequestMapping("/topics")
@Tag(name = "Topic subscriptions", description = "Subscribe and unsubscribe to topics")
public class TopicSubscriptionController {

	private final ISubscriptionService subscriptionService;

	public TopicSubscriptionController(ISubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	@GetMapping("/subscribed")
	@Operation(summary = "List subscribed topics", description = "Returns topics subscribed by current user")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Subscribed topics returned"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public List<TopicDto> getSubscribedTopics() {
		return subscriptionService.getSubscribedTopics();
	}

	@PostMapping("/{id}/subscribe")
	@Operation(summary = "Subscribe to topic", description = "Subscribes current user to topic")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Subscribed successfully"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "Topic not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "409", description = "Already subscribed",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> subscribe(@PathVariable Long id) {
		subscriptionService.subscribe(id);
		
		return ResponseEntity.ok(new MessageResponse("Abonnement effectué avec succès."));
	}

	@DeleteMapping("/{id}/unsubscribe")
	@Operation(summary = "Unsubscribe from topic", description = "Removes current user subscription from topic")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Unsubscribed successfully"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "Topic not found or subscription missing",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> unsubscribe(@PathVariable Long id) {
		subscriptionService.unsubscribe(id);

		return ResponseEntity.ok(new MessageResponse("Désabonnement effectué avec succès."));
	}
}
