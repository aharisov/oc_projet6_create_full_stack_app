package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.payload.response.UserProfileResponse;

/**
 * Current user use cases for identity resolution and profile management.
 */
public interface IUserService {
	/**
	 * Resolves the currently authenticated user from the security context.
	 *
	 * @return authenticated user entity
	 */
    User getCurrentUser();

	/**
	 * Returns a profile representation of the current user.
	 *
	 * @return profile response
	 */
    UserProfileResponse getUserProfile();

	/**
	 * Updates current user fields when values have changed.
	 *
	 * @param request update payload
	 */
    void updateUser(UpdateUserRequest request);
}
