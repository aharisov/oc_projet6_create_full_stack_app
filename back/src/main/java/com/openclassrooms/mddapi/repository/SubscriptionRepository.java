package com.openclassrooms.mddapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Topic;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	boolean existsByUserIdAndTopicId(Long userId, Long topicId);
	Optional<Subscription> findByUserIdAndTopicId(Long userId, Long topicId);

	@Query("SELECT s.topic FROM Subscription s WHERE s.user.id = :userId")
	List<Topic> findSubscribedTopicsByUserId(@Param("userId") Long userId);
}
