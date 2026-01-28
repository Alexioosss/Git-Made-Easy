package com.gitmadeeasy.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;

public interface UserSchemaMapper {
    UserSchema toSchema(User user);
    User toEntity(UserSchema userSchema);
}