package com.openclassrooms.mddapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.ISubscriptionService;

@RestController
@RequestMapping("/topics")
public class TopicSubscriptionController {

	private final ISubscriptionService subscriptionService;

	public TopicSubscriptionController(ISubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	@PostMapping("/{id}/subscribe")
	public ResponseEntity<MessageResponse> subscribe(@PathVariable Long id) {
		subscriptionService.subscribe(id);
		
		return ResponseEntity.ok(new MessageResponse("Subscribed successfully!"));
	}

	@DeleteMapping("/{id}/unsubscribe")
	public ResponseEntity<MessageResponse> unsubscribe(@PathVariable Long id) {
		subscriptionService.unsubscribe(id);

		return ResponseEntity.ok(new MessageResponse("Unsubscribed successfully!"));
	}
}
