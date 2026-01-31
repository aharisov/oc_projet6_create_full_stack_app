package com.openclassrooms.mddapi.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JwtService {

	private final SecretKey secretKey;
	private final long accessTokenExpirationMs;

	public JwtService(
		@Value("${JWT_SECRET}") String jwtSecret,
		@Value("${app.jwt.access-token-expiration}") long accessTokenExpirationMs
	) {
		this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpirationMs = accessTokenExpirationMs;
	}

	public String generateAccessToken(User user) {
		return generateToken(user, accessTokenExpirationMs);
	}

	public long getAccessTokenExpirationMs() {
		return accessTokenExpirationMs;
	}

	private String generateToken(User user, long expirationMs) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMs);

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		claims.put("username", user.getUsername());

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(String.valueOf(user.getId()))
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
