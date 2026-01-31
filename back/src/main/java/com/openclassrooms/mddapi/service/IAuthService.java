package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserRegisterDto;

public interface IAuthService {
    Boolean register(UserRegisterDto user);
}
