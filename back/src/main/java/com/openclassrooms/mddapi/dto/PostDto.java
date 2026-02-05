package com.openclassrooms.mddapi.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostDto {
	private Long id;

	@NotNull(message = "Topic id is required")
	private Long topicId;
	private Long authorId;

	@NotBlank(message = "Post title is required")
	@Size(max = 200, message = "Max post title length is 200 characters.")
	private String title;

	@NotBlank(message = "Post content is required")
	private String content;
	private Instant createdAt;
	private Instant updatedAt;

	public PostDto(Long id, Long topicId, Long authorId, String title, 
		String content, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.topicId = topicId;
		this.authorId = authorId;
		this.title = title;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Long getTopicId() { return topicId; }
	public void setTopicId(Long topicId) { this.topicId = topicId; }

	public Long getAuthorId() { return authorId; }
	public void setAuthorId(Long authorId) { this.authorId = authorId; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }

	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	public Instant getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
