package com.gitmadeeasy.entities.users;

import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;

public interface UserGateway {
    void createUser(UserSchema request);
    void getUser(Long userId);
    void deleteUser(Long userId);
}