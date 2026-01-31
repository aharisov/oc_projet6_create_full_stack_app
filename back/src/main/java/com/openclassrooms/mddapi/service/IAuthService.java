package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.payload.request.SignupRequest;

public interface IAuthService {
    Boolean register(SignupRequest request);
}
