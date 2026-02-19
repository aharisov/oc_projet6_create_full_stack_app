package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignupRequest;
import com.openclassrooms.mddapi.security.AuthTokens;

/**
 * Authentication use cases: register, login, refresh, and logout.
 */
public interface IAuthService {
	/**
	 * Registers a user after validating uniqueness constraints.
	 *
	 * @param request signup payload
	 * @return {@code true} when registration succeeds
	 */
    Boolean register(SignupRequest request);

	/**
	 * Authenticates a user and issues access/refresh tokens.
	 *
	 * @param request login payload
	 * @return issued authentication tokens
	 */
    AuthTokens login(LoginRequest request);

	/**
	 * Issues a new token pair using a valid refresh token.
	 *
	 * @param refreshToken refresh token from cookie
	 * @return renewed authentication tokens
	 */
	AuthTokens refresh(String refreshToken);

	/**
	 * Invalidates the current refresh token.
	 *
	 * @param refreshToken refresh token from cookie
	 */
	void logout(String refreshToken);
}
