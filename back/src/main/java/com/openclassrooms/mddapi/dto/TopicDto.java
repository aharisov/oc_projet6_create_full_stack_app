package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class TopicDto {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Topic identifier", example = "4")
	private Long id;

	@NotBlank(message = "Topic name is required")
	@Size(max = 200, message = "Max topic name length is 200 characters.")
	@Schema(description = "Topic name", example = "Java")
	private String name;

	@Size(max = 2000, message = "Max description length is 2000 characters.")
	@Schema(description = "Topic description", example = "Everything about Java development")
	private String description;

	public TopicDto(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
}
