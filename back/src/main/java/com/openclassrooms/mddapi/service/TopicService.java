package com.openclassrooms.mddapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.exception.ConflictException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;

@Service
public class TopicService implements ITopicService {
	private static final Logger log = LoggerFactory.getLogger(TopicService.class);

	private final TopicRepository topicRepository;
	private final TopicMapper topicMapper;
	
	public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
		this.topicRepository = topicRepository;
		this.topicMapper = topicMapper;
	}

	@Override
	public List<TopicDto> getTopics() {
		return topicMapper.toDto(topicRepository.findAll());
	}

	@Override
	public void createTopic(TopicDto request) {
		Topic topic = topicMapper.toEntity(request);

		try {
			Topic newTopic = topicRepository.save(topic);
			log.info("Topic created: id={}", newTopic.getId());
		} catch (DataIntegrityViolationException ex) {
			log.warn("Topic create failed: name already exists");
			throw new ConflictException("Topic name already exists");
		}
	}

	@Override
	public void updateTopic(Long id, TopicDto request) {
		Topic topic = topicRepository.findById(id)
			.orElseThrow(() -> {
				log.warn("Topic with id {} not found", id);
				return new NotFoundException("Topic not found");
			});
		
		topic.setName(request.getName());
		topic.setDescription(request.getDescription());

		try {
			Topic updatedTopic = topicRepository.save(topic);
			log.info("Topic updated: id={}", updatedTopic.getId());
		} catch (DataIntegrityViolationException ex) {
			log.warn("Topic update failed: name already exists");
			throw new ConflictException("Topic name already exists");
		}
	}
}
