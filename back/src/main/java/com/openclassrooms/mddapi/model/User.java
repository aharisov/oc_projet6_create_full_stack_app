package com.openclassrooms.mddapi.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
	private String email;

    @Column(nullable = false, unique = true, length = 30)
    private String username;
    
	@Column(name="password_hash", nullable = false)
	private String passwordHash;
    
    @Column(name="created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private Instant updatedAt;

}