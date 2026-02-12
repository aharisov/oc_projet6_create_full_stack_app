package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;

@Service
public class PostService implements IPostService {
	private static final Logger log = LoggerFactory.getLogger(PostService.class);

	private final PostRepository postRepository;
	private final TopicRepository topicRepository;
	private final PostMapper postMapper;
	private final UserService userService;
	
	public PostService(
		PostRepository postRepository,
		TopicRepository topicRepository,
		PostMapper postMapper,
		UserService userService
	) {
		this.postRepository = postRepository;
		this.topicRepository = topicRepository;
		this.postMapper = postMapper;
		this.userService = userService;
	}

	@Override
	public List<PostDto> getFeed(String sortOrder) {
		Long userId = userService.getCurrentUser().getId();
		if ("asc".equalsIgnoreCase(sortOrder)) {
			return postMapper.toDto(postRepository.findAllSubscribedByUserIdOrderByCreatedAtAsc(userId));
		}

		return postMapper.toDto(postRepository.findAllSubscribedByUserIdOrderByCreatedAtDesc(userId));
	}

	@Override
	public PostDto getPost(Long id) {
		Long userId = userService.getCurrentUser().getId();
		Post post = postRepository.findSubscribedPostByIdAndUserId(Objects.requireNonNull(id), userId)
			.orElseThrow(() -> {
				log.warn("Post with id {} not found for user {}", id, userId);
				return new NotFoundException("Post not found");
			});

		Long topicId = post.getTopic() != null ? post.getTopic().getId() : null;
		Long authorId = post.getAuthor() != null ? post.getAuthor().getId() : null;

		PostDto postData = postMapper.toDto(post);
		postData.setTopicId(topicId);
		postData.setAuthorId(authorId);

		return postData;
	}

	@Override
	public void createPost(PostDto request) {
		User author = userService.getCurrentUser();
		Topic topic = topicRepository.findById(Objects.requireNonNull(request.getTopicId()))
			.orElseThrow(() -> {
				log.warn("Topic with id {} not found", request.getTopicId());
				return new NotFoundException("Topic not found");
			});

		Post post = postMapper.toEntity(request);
		post.setTopic(topic);
		post.setAuthor(author);
		
		Post savedPost = postRepository.save(post);
		log.info("Post created: id={}", savedPost.getId());
	}

	@Override
	public Boolean isPostExists(Long postId) {
		if (postId == null) {
			throw new BadRequestException("Post id is required");
		}

		postRepository.findById(postId)
			.orElseThrow(() -> {
				log.warn("Post with id {} not found", postId);
				return new NotFoundException("Post not found");
			});

		return true;
	}
}
