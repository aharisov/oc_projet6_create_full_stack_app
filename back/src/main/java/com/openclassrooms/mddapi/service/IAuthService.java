package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignupRequest;
import com.openclassrooms.mddapi.payload.response.AuthResponse;

public interface IAuthService {
    Boolean register(SignupRequest request);
    AuthResponse login(LoginRequest request);
}
