package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.ITopicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/topics")
public class TopicController {
	
	private ITopicService topicService;
	
	public TopicController(ITopicService topicService) {
		this.topicService = topicService;		
	}

	@GetMapping
	public List<TopicDto> getTopics() {
		return topicService.getTopics();
	}

	@PostMapping
	public ResponseEntity<MessageResponse> createTopic(@Valid @RequestBody TopicDto request) {
		topicService.createTopic(request);

		return ResponseEntity.status(201)
			.body(new MessageResponse("Topic created successfully!"));
	}

	@PutMapping("/{id}")
	public ResponseEntity<MessageResponse> updateTopic(
		@PathVariable Long id, 
		@Valid @RequestBody TopicDto request
	) {
		topicService.updateTopic(id, request);
		
		return ResponseEntity.ok()
			.body(new MessageResponse("Topic updated successfully!"));
	}
	
}
