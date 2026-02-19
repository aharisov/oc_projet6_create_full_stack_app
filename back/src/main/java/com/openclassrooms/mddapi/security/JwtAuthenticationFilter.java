package com.openclassrooms.mddapi.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Resolves access tokens from the Authorization header and populates the security context.
 *
 * <p>Invalid tokens are ignored and the filter chain continues without authentication.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtService jwtService;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}

	/**
	 * Authenticates the request when a valid access token is present.
	 *
	 * @param request current request
	 * @param response current response
	 * @param filterChain remaining filters
	 * @throws ServletException when servlet filtering fails
	 * @throws IOException when IO operations fail
	 */
	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	)
		throws ServletException, IOException {

		String token = resolveToken(request);
		if (token != null && jwtService.isTokenValid(token) && "access".equals(jwtService.getTokenType(token))) {
			String subject = jwtService.getSubject(token);
			if (subject != null) {
				userRepository.findById(Objects.requireNonNull(Long.valueOf(subject))).ifPresent(user -> {
					UsernamePasswordAuthenticationToken authentication = buildAuthentication(user, request);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				});
			}
		} else if (token != null) {
			log.debug("JWT token rejected for request: {} {}", request.getMethod(), request.getRequestURI());
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Extracts bearer token from the Authorization header.
	 *
	 * @param request current request
	 * @return token without {@code Bearer } prefix, or {@code null} when missing
	 */
	private String resolveToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

	/**
	 * Builds a stateless authentication object for Spring Security context.
	 *
	 * @param user authenticated user
	 * @param request current request
	 * @return authentication token using user email as principal
	 */
	private UsernamePasswordAuthenticationToken buildAuthentication(User user, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList());
		
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		return authentication;
	}
}
