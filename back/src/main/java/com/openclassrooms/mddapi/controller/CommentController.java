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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

	private final ICommentService commentService;

	public CommentController(ICommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping
	public List<CommentDto> getComments(@PathVariable Long postId) {
		return commentService.getComments(postId);
	}

	@PostMapping
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
