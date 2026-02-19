package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.CommentDto;

/**
 * Comment use cases for reading and creating comments on posts.
 */
public interface ICommentService {
	/**
	 * Returns comments for a post after access checks.
	 *
	 * @param idPost target post id
	 * @return ordered comments for the post
	 */
	List<CommentDto> getComments(Long idPost);

	/**
	 * Creates a new comment for a post.
	 *
	 * @param request comment payload
	 */
	void createComment(CommentDto request);
}
