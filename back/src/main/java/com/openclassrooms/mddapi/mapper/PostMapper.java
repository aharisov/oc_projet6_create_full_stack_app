package com.openclassrooms.mddapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;

@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDto, Post> {
	@Override
	@Mapping(target = "topic", expression = "java(toTopic(dto.getTopicId()))")
	@Mapping(target = "author", expression = "java(toUser(dto.getAuthorId()))")
	Post toEntity(PostDto dto);

	@Override
	@Mapping(target = "topicId", source = "topic.id")
	@Mapping(target = "topicName", source = "topic.name")
	@Mapping(target = "authorId", source = "author.id")
	@Mapping(target = "authorName", source = "author.username")
	PostDto toDto(Post entity);

	default Topic toTopic(Long id) {
		if (id == null) {
			return null;
		}
		Topic topic = new Topic();
		topic.setId(id);
		return topic;
	}

	default User toUser(Long id) {
		if (id == null) {
			return null;
		}
		User user = new User();
		user.setId(id);
		return user;
	}
}
