package com.openclassrooms.mddapi.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JwtService {

	private final SecretKey secretKey;
	private final long accessTokenExpirationMs;
	private final long refreshTokenExpirationMs;

	public JwtService(
		@Value("${JWT_SECRET}") String jwtSecret,
		@Value("${app.jwt.access-token-expiration}") long accessTokenExpirationMs,
		@Value("${app.jwt.refresh-token-expiration}") long refreshTokenExpirationMs
	) {
		this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpirationMs = accessTokenExpirationMs;
		this.refreshTokenExpirationMs = refreshTokenExpirationMs;
	}

	public String generateAccessToken(User user) {
		return generateToken(user, accessTokenExpirationMs, "access");
	}

	public String generateRefreshToken(User user) {
		return generateToken(user, refreshTokenExpirationMs, "refresh");
	}

	public long getAccessTokenExpirationMs() {
		return accessTokenExpirationMs;
	}

	public long getRefreshTokenExpirationMs() {
		return refreshTokenExpirationMs;
	}

	public boolean isTokenValid(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}

	public String getTokenType(String token) {
		Object type = parseClaims(token).get("type");
		return type != null ? type.toString() : null;
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private String generateToken(User user, long expirationMs, String type) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMs);

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		claims.put("username", user.getUsername());
		claims.put("type", type);

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(String.valueOf(user.getId()))
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
