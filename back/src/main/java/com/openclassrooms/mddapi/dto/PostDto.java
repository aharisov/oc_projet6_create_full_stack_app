package com.openclassrooms.mddapi.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class PostDto {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post identifier", example = "10")
	private Long id;

	@NotNull(message = "L'identifiant du thème est requis.")
	@Schema(description = "Topic identifier", example = "4")
	private Long topicId;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post topic name", example = "Java")
	private String topicName;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post author identifier", example = "1")
	private Long authorId;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post author username", example = "johndoe")
	private String authorName;

	@NotBlank(message = "Le titre de l'article est requis.")
	@Size(max = 200, message = "Le titre de l'article ne peut pas dépasser 200 caractères.")
	@Schema(description = "Post title", example = "Titre de l’article")
	private String title;

	@NotBlank(message = "Le contenu de l'article est requis.")
	@Schema(description = "Post content", example = "Contenu de l’article")
	private String content;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Creation timestamp")
	private Instant createdAt;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Last update timestamp")
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

	public String getTopicName() { return topicName; }
	public void setTopicName(String topicName) { this.topicName = topicName; }

	public Long getAuthorId() { return authorId; }
	public void setAuthorId(Long authorId) { this.authorId = authorId; }

	public String getAuthorName() { return authorName; }
	public void setAuthorName(String authorName) { this.authorName = authorName; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }

	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	public Instant getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
