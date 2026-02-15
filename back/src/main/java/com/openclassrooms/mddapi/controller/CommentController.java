package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.ICommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts/{postId}/comments")
@Tag(name = "Comments", description = "Comment endpoints linked to posts")
public class CommentController {

	private final ICommentService commentService;

	public CommentController(ICommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping
	@Operation(summary = "Get comments", description = "Returns comments for a given post")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Comments returned"),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "Post not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public List<CommentDto> getComments(@PathVariable Long postId) {
		return commentService.getComments(postId);
	}

	@PostMapping
	@Operation(summary = "Create comment", description = "Adds a comment to a post")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		required = true,
		content = @Content(
			schema = @Schema(implementation = CommentDto.class),
			examples = @ExampleObject(
				name = "Create comment",
				value = "{\"content\":\"Très bon article.\"}"
			)
		)
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Comment created"),
		@ApiResponse(responseCode = "400", description = "Invalid request data",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "404", description = "Post not found",
			content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	public ResponseEntity<MessageResponse> createComment(
		@PathVariable Long postId,
		@Valid @RequestBody CommentDto request
	) {
		request.setPostId(postId);
		commentService.createComment(request);

		return ResponseEntity.status(201)
			.body(new MessageResponse("Comment created successfully!"));
	}
}
