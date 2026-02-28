package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.IPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Exposes post feed and single-post endpoints for authenticated users.
 *
 * <p>Creation assigns author and creation date server-side.</p>
 */
@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Post feed and article endpoints")
public class PostController {
	private final IPostService postService;

	public PostController(IPostService postService) {
		this.postService = postService;
	}

	@GetMapping
	@Operation(summary = "Get feed", description = "Returns posts sorted by date (sort=asc or desc)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Feed returned"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public List<PostDto> getFeed(
		@RequestParam(name = "sort", required = false) 
		String sort
	) {
		return postService.getFeed(sort);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get post", description = "Returns a single post by id")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Post returned"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "Post not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public PostDto getPost(
		@PathVariable 
		Long id
	) {
		return postService.getPost(id);
	}

	@PostMapping
	@Operation(summary = "Create post", description = "Creates a new post with automatic author and date")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		required = true,
		content = @Content(
			schema = @Schema(implementation = PostDto.class),
			examples = @ExampleObject(
				name = "Create post",
				value = "{\"topicId\":4,\"title\":\"Titre de l’article\",\"content\":\"Contenu de l’article\"}"
			)
		)
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Post created"),
		@ApiResponse(responseCode = "400", description = "Invalid request data",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "Topic not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "409", description = "Post title already exists",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> createPost(
		@Valid 
		@RequestBody 
		PostDto request
	) {
		postService.createPost(request);

		return ResponseEntity.status(201)
			.body((new MessageResponse("Article créé avec succès.")));
	}

}
