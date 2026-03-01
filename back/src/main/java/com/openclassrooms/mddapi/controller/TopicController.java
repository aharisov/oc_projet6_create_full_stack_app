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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Provides topic listing and management endpoints.
 *
 * <p>Conflict and not-found scenarios are delegated to the topic service.</p>
 */
@RestController
@RequestMapping("/topics")
@Tag(name = "Topics", description = "Topic management endpoints")
public class TopicController {
	
	private ITopicService topicService;
	
	public TopicController(ITopicService topicService) {
		this.topicService = topicService;		
	}

	@GetMapping
	@Operation(summary = "List topics", description = "Returns all available topics")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Topics returned"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public List<TopicDto> getTopics() {
		return topicService.getTopics();
	}

	@PostMapping
	@Operation(summary = "Create topic", description = "Creates a new topic")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		required = true,
		content = @Content(
			schema = @Schema(implementation = TopicDto.class),
			examples = @ExampleObject(
				name = "Create topic",
				value = "{\"name\":\"Java\",\"description\":\"Everything about Java development\"}"
			)
		)
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Topic created"),
		@ApiResponse(responseCode = "400", description = "Invalid request data",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "409", description = "Topic name already exists",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> createTopic(@Valid @RequestBody TopicDto request) {
		topicService.createTopic(request);

		return ResponseEntity.status(201)
			.body(new MessageResponse("Thème créé avec succès."));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update topic", description = "Updates topic name and description")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		required = true,
		content = @Content(
			schema = @Schema(implementation = TopicDto.class),
			examples = @ExampleObject(
				name = "Update topic",
				value = "{\"name\":\"Java avancé\",\"description\":\"Threads, JVM et performance\"}"
			)
		)
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Topic updated"),
		@ApiResponse(responseCode = "400", description = "Invalid request data",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "Topic not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "409", description = "Topic name already exists",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> updateTopic(
		@PathVariable Long id, 
		@Valid @RequestBody TopicDto request
	) {
		topicService.updateTopic(id, request);
		
		return ResponseEntity.ok()
			.body(new MessageResponse("Thème mis à jour avec succès."));
	}
	
}
