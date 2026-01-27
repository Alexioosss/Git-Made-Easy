package com.gitmadeeasy.infrastructure.factories.users;

import com.gitmadeeasy.infrastructure.dto.users.UserRequest;
import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;

public class UserFactoryImplementation implements UserFactory {

    @Override
    public UserSchema createSchemaFromRequest(UserRequest request) {
        return new UserSchema(
                request.firstName(),
                request.lastName(),
                request.emailAddress()
        );
    }
}