package com.openclassrooms.mddapi.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public class CommentDto {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment identifier", example = "7")
	private Long id;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Post identifier", example = "10")
	private Long postId;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment author identifier", example = "1")
	private Long authorId;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Comment author username", example = "johndoe")
	private String authorName;

	@NotBlank(message = "Le contenu du commentaire est requis.")
	@Schema(description = "Comment content", example = "Très bon article.")
	private String content;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Creation timestamp")
	private Instant createdAt;

	public CommentDto(Long id, Long postId, Long authorId, String content, Instant createdAt) {
		this.id = id;
		this.postId = postId;
		this.authorId = authorId;
		this.content = content;
		this.createdAt = createdAt;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Long getPostId() { return postId; }
	public void setPostId(Long postId) { this.postId = postId; }

	public Long getAuthorId() { return authorId; }
	public void setAuthorId(Long authorId) { this.authorId = authorId; }

	public String getAuthorName() { return authorName; }
	public void setAuthorName(String authorName) { this.authorName = authorName; }

	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }

	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
