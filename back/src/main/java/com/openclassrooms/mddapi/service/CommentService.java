package com.openclassrooms.mddapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;

/**
 * Handles comment retrieval and creation for posts visible to the current user.
 */
@Service
public class CommentService implements ICommentService {
	private static final Logger log = LoggerFactory.getLogger(CommentService.class);

	private final CommentRepository commentRepository;
	private final PostService postService;
	private final CommentMapper commentMapper;
	private final UserService userService;

	public CommentService(
		CommentRepository commentRepository,
		PostService postService,
		CommentMapper commentMapper,
		UserService userService
	) {
		this.commentRepository = commentRepository;
		this.postService = postService;
		this.commentMapper = commentMapper;
		this.userService = userService;
	}

	/**
	 * Returns all comments for a post ordered by creation date.
	 *
	 * @param idPost target post id
	 * @return ordered post comments
	 * @throws com.openclassrooms.mddapi.exception.NotFoundException when the post does not exist
	 */
	@Override
	public List<CommentDto> getComments(Long idPost) {
		postService.isPostExists(idPost);
		
		return commentMapper.toDto(commentRepository.findAllByPostIdOrderByCreatedAtAsc(idPost));
	}

	/**
	 * Creates a comment authored by the current user.
	 *
	 * @param request comment payload
	 * @throws BadRequestException when payload is missing
	 * @throws com.openclassrooms.mddapi.exception.NotFoundException when the target post does not exist
	 */
	@Override
	public void createComment(CommentDto request) {
		if (request == null) {
			throw new BadRequestException("Les données du commentaire sont requises.");
		}

		postService.isPostExists(request.getPostId());
		User author = userService.getCurrentUser();

		Comment comment = commentMapper.toEntity(request);
		comment.setAuthor(author);

		Comment savedComment = commentRepository.save(comment);
		log.info("Comment created: id={}, postId={}", savedComment.getId(), request.getPostId());
	}

}
