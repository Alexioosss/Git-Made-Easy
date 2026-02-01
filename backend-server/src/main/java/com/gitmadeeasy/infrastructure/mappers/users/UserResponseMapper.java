package com.gitmadeeasy.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmailAddress()
        );
    }
}