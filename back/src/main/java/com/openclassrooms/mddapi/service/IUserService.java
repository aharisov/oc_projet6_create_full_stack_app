package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.payload.response.UserProfileResponse;

public interface IUserService {
    User getCurrentUser();
    UserProfileResponse getUserProfile();
    void updateUser(UpdateUserRequest request);
}
