package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.TopicDto;

public interface ISubscriptionService {
	void subscribe(Long id);
	void unsubscribe(Long id);
	List<TopicDto> getSubscribedTopics();
}
