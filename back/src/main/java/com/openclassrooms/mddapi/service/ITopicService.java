package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.TopicDto;

public interface ITopicService {

	List<TopicDto> getTopics();
	void createTopic(TopicDto request);
	void updateTopic(Long id, TopicDto request);

}
