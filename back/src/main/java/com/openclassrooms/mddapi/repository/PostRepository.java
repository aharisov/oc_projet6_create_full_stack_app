package com.openclassrooms.mddapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	@Query("SELECT p FROM Post p JOIN Subscription s ON s.topic.id = p.topic.id "
		+ "WHERE s.user.id = :userId ORDER BY p.createdAt DESC")
	List<Post> findAllSubscribedByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

	@Query("SELECT p FROM Post p JOIN Subscription s ON s.topic.id = p.topic.id "
		+ "WHERE s.user.id = :userId ORDER BY p.createdAt ASC")
	List<Post> findAllSubscribedByUserIdOrderByCreatedAtAsc(@Param("userId") Long userId);

	@Query("SELECT p FROM Post p JOIN Subscription s ON s.topic.id = p.topic.id "
		+ "WHERE p.id = :postId AND s.user.id = :userId")
	Optional<Post> findSubscribedPostByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
