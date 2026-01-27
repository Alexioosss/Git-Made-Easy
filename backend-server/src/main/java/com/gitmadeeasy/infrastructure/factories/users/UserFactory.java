package com.gitmadeeasy.infrastructure.factories.users;

import com.gitmadeeasy.infrastructure.dto.users.UserRequest;
import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;

public interface UserFactory {
    UserSchema createSchemaFromRequest(UserRequest request);
}