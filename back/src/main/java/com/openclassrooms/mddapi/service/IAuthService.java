package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignupRequest;
import com.openclassrooms.mddapi.security.AuthTokens;

public interface IAuthService {
    Boolean register(SignupRequest request);
    AuthTokens login(LoginRequest request);
	AuthTokens refresh(String refreshToken);
	void logout(String refreshToken);
}
