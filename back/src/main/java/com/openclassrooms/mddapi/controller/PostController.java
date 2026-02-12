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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final IPostService postService;

	public PostController(IPostService postService) {
		this.postService = postService;
	}

	@GetMapping
	public List<PostDto> getFeed(
		@RequestParam(name = "sort", required = false) 
		String sort
	) {
		return postService.getFeed(sort);
	}

	@GetMapping("/{id}")
	public PostDto getPost(
		@PathVariable 
		Long id
	) {
		return postService.getPost(id);
	}

	@PostMapping
	public ResponseEntity<MessageResponse> createPost(
		@Valid 
		@RequestBody 
		PostDto request
	) {
		postService.createPost(request);

		return ResponseEntity.status(201)
			.body((new MessageResponse("Post created successfully!")));
	}

}
