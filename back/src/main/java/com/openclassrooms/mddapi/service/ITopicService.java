package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.TopicDto;

/**
 * Topic use cases for listing and management.
 */
public interface ITopicService {

	/**
	 * Returns all topics.
	 *
	 * @return available topics
	 */
	List<TopicDto> getTopics();

	/**
	 * Creates a new topic.
	 *
	 * @param request topic payload
	 */
	void createTopic(TopicDto request);

	/**
	 * Updates an existing topic.
	 *
	 * @param id topic id
	 * @param request topic payload
	 */
	void updateTopic(Long id, TopicDto request);

}
