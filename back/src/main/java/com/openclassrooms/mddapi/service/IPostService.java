package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.PostDto;

public interface IPostService {
	List<PostDto> getFeed(String sortOrder);
	PostDto getPost(Long id);
	void createPost(PostDto request);
	Boolean isPostExists(Long id);
}
