package com.openclassrooms.mddapi.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="post_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "topic_id", nullable = false)
	private Topic topic;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private User author;
	
	@Column(nullable = false, length = 200)
	private String title;

	@Column(columnDefinition = "LONGTEXT", nullable = false)
	private String content;

	@Column(name="created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private Instant updatedAt;
}
