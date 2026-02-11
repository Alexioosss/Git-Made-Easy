package com.gitmadeeasy.unit.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSchemaMapperTest {
    private static final UserSchemaMapper mapper = new UserSchemaMapper();

    @Test
    @DisplayName("To Schema - Maps User Domain Entity To Persistence Schema")
    void toSchema_WhenGivenDomainEntity_MapsToPersistenceSchema() {
        // Arrange
        User user = new User("John", "Doe", "myemail1@gmail.com", "HashedMyPassword123'");

        // Act
        UserSchema schema = mapper.toSchema(user);

        // Assert
        assertEquals("John", schema.getFirstName());
        assertEquals("Doe", schema.getLastName());
        assertEquals("myemail1@gmail.com", schema.getEmailAddress());
        assertEquals("HashedMyPassword123'", schema.getPassword());
    }

    @Test
    @DisplayName("To Entity - Maps Persistence Schema To User Domain Entity")
    void toEntity_WhenGivenPersistenceSchema_MapsToDomainEntity() {
        // Arrange
        UserSchema schema = new UserSchema("John", "Doe", "myemail1@gmail.com", "HashedMyPassword123'");

        // Act
        User user = mapper.toEntity(schema);

        // Assert
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("myemail1@gmail.com", user.getEmailAddress());
        assertEquals("HashedMyPassword123'", user.getPassword());
    }
}