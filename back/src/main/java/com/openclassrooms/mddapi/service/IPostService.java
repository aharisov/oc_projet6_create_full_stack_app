package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.PostDto;

/**
 * Post use cases for feed retrieval, post details, and creation.
 */
public interface IPostService {
	/**
	 * Returns subscribed feed posts sorted by creation date.
	 *
	 * @param sortOrder optional sort order: {@code asc} or {@code desc}
	 * @return feed posts visible to the current user
	 */
	List<PostDto> getFeed(String sortOrder);

	/**
	 * Returns one post if it is visible to the current user.
	 *
	 * @param id post id
	 * @return post details
	 */
	PostDto getPost(Long id);

	/**
	 * Creates a new post for the current user.
	 *
	 * @param request post payload
	 */
	void createPost(PostDto request);

	/**
	 * Validates that a post exists.
	 *
	 * @param id post id
	 * @return {@code true} when the post exists
	 */
	Boolean isPostExists(Long id);
}
