package com.openclassrooms.mddapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	boolean existsByUserIdAndTopicId(Long userId, Long topicId);
	Optional<Subscription> findByUserIdAndTopicId(Long userId, Long topicId);
}
