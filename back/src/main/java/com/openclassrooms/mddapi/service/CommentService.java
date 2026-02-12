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

	@Override
	public List<CommentDto> getComments(Long idPost) {
		postService.isPostExists(idPost);
		
		return commentMapper.toDto(commentRepository.findAllByPostIdOrderByCreatedAtAsc(idPost));
	}

	@Override
	public void createComment(CommentDto request) {
		if (request == null) {
			throw new BadRequestException("Comment payload is required");
		}

		postService.isPostExists(request.getPostId());
		User author = userService.getCurrentUser();

		Comment comment = commentMapper.toEntity(request);
		comment.setAuthor(author);

		Comment savedComment = commentRepository.save(comment);
		log.info("Comment created: id={}, postId={}", savedComment.getId(), request.getPostId());
	}

}
