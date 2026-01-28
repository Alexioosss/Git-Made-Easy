package com.gitmadeeasy.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;

public class UserSchemaMapperImplementation implements UserSchemaMapper {

    @Override
    public UserSchema toSchema(User user) {
        return new UserSchema(
            user.getFirstName(),
            user.getLastName(),
            user.getEmailAddress()
        );
    }

    @Override
    public User toEntity(UserSchema userSchema) {
        return new User(
            userSchema.getId(),
            userSchema.getFirstName(),
            userSchema.getLastName(),
            userSchema.getEmailAddress()
        );
    }
}