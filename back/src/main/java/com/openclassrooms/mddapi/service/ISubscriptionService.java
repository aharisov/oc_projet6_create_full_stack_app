package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.TopicDto;

/**
 * Subscription use cases between the current user and topics.
 */
public interface ISubscriptionService {
	/**
	 * Subscribes the current user to a topic.
	 *
	 * @param id topic id
	 */
	void subscribe(Long id);

	/**
	 * Removes the current user subscription from a topic.
	 *
	 * @param id topic id
	 */
	void unsubscribe(Long id);

	/**
	 * Returns all topics currently subscribed by the user.
	 *
	 * @return subscribed topics
	 */
	List<TopicDto> getSubscribedTopics();
}
