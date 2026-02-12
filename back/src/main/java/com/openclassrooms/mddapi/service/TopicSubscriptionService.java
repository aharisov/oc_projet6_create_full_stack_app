package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.ConflictException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;

@Service
public class TopicSubscriptionService implements ISubscriptionService {
	private static final Logger log = LoggerFactory.getLogger(TopicSubscriptionService.class);

	private final TopicRepository topicRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final TopicMapper topicMapper;
	private final UserService userService;
	
	public TopicSubscriptionService(
		TopicRepository topicRepository,
		SubscriptionRepository subscriptionRepository,
		TopicMapper topicMapper,
		UserService userService
	) {
		this.topicRepository = topicRepository;
		this.subscriptionRepository = subscriptionRepository;
		this.topicMapper = topicMapper;
		this.userService = userService;
	}

	@Override
	@Transactional
	public void subscribe(Long topicId) {
		if (topicId == null) {
			throw new BadRequestException("Topic id is required");
		}

		Topic topic = topicRepository.findById(topicId)
			.orElseThrow(() -> {
				log.warn("Topic with id {} not found", topicId);
				return new NotFoundException("Topic not found");
			});
		User user = userService.getCurrentUser();

		if (subscriptionRepository.existsByUserIdAndTopicId(user.getId(), topicId)) {
			log.warn("Subscription already exists: userId={}, topicId={}", user.getId(), topicId);
			throw new ConflictException("Already subscribed to this topic");
		}

		Subscription subscription = new Subscription();
		subscription.setUser(user);
		subscription.setTopic(topic);

		subscriptionRepository.save(Objects.requireNonNull(subscription));
		log.info("User {} subscribed to topic {}", user.getId(), topicId);
	}

	@Override
	public List<TopicDto> getSubscribedTopics() {
		Long userId = userService.getCurrentUser().getId();
		
		return topicMapper.toDto(subscriptionRepository.findSubscribedTopicsByUserId(userId));
	}

	@Override
	@Transactional
	public void unsubscribe(Long topicId) {
		if (topicId == null) {
			throw new BadRequestException("Topic id is required");
		}
		if (!topicRepository.existsById(topicId)) {
			log.warn("Topic with id {} not found", topicId);
			throw new NotFoundException("Topic not found");
		}

		User user = userService.getCurrentUser();
		Subscription subscription = subscriptionRepository.findByUserIdAndTopicId(user.getId(), topicId)
			.orElseThrow(() -> {
				log.warn("Subscription not found: userId={}, topicId={}", user.getId(), topicId);
				return new NotFoundException("Subscription not found");
			});
			
		subscriptionRepository.delete(Objects.requireNonNull(subscription));
		log.info("User {} unsubscribed from topic {}", user.getId(), topicId);
	}
}
