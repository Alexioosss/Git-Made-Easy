package com.gitmadeeasy.unit.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserResponseMapperTest {
    private static final UserResponseMapper mapper = new UserResponseMapper();

    @Test
    @DisplayName("To User Response - Maps User Entity To Public UserResponse")
    void toUserResponse_WhenGivenDomainEntity_ReturnsPublicUserResponse() {
        // Arrange
        User user = new User("1", "John", "Doe", "myemail1@gmail.com", "MyPassword123'");

        // Act
        UserResponse response = mapper.toUserResponse(user);

        // Assert
        assertEquals("1", response.id());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("myemail1@gmail.com", response.emailAddress());
    }
}