package com.gitmadeeasy.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert a domain User entity object to a public-facing user response objet, not exposing sensitive fields
 */
@Component
public class UserResponseMapper {
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmailAddress());
    }
}