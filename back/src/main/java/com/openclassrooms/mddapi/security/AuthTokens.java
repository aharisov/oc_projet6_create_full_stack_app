package com.openclassrooms.mddapi.security;

public class AuthTokens {
	private final String accessToken;
	private final String refreshToken;
	private final long accessTokenExpiresIn;
	private final long refreshTokenExpiresIn;

	public AuthTokens(String accessToken, String refreshToken, long accessTokenExpiresIn, long refreshTokenExpiresIn) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresIn = accessTokenExpiresIn;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}

	public String getAccessToken() { return accessToken; }
	public String getRefreshToken() { return refreshToken; }
	public long getAccessTokenExpiresIn() { return accessTokenExpiresIn; }
	public long getRefreshTokenExpiresIn() { return refreshTokenExpiresIn; }
}
