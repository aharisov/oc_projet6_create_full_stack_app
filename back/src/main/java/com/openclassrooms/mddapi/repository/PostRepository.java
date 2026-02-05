package com.openclassrooms.mddapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findAllByOrderByCreatedAtDesc();
	List<Post> findAllByOrderByCreatedAtAsc();
}
