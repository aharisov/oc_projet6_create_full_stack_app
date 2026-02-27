package com.openclassrooms.mddapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDto, Comment> {

	@Override
	@Mapping(target = "post", expression = "java(toPost(dto.getPostId()))")
	@Mapping(target = "author", expression = "java(toUser(dto.getAuthorId()))")
	Comment toEntity(CommentDto dto);

	@Override
	@Mapping(target = "postId", source = "post.id")
	@Mapping(target = "authorId", source = "author.id")
	@Mapping(target = "authorName", source = "author.username")
	CommentDto toDto(Comment entity);

	default Post toPost(Long id) {
		if (id == null) {
			return null;
		}
		Post post = new Post();
		post.setId(id);
		return post;
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
