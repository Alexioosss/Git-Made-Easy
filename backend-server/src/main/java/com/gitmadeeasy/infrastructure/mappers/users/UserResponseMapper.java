package com.gitmadeeasy.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;

public interface UserResponseMapper {
    UserResponse toUserResponse(User user);
}