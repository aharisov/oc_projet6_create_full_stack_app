package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.PostDto;

public interface IPostService {
	List<PostDto> getPosts(String sortOrder);
	PostDto getPost(Long id);
	void createPost(PostDto request);
	// TODO: addComment()
}
