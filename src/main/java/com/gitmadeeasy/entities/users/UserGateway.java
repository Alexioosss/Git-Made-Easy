package com.gitmadeeasy.entities.users;

import com.gitmadeeasy.infrastructure.dto.users.UserRequest;

public interface UserGateway {
    void createUser(UserRequest request);
    void getUser(Long userId);
    void deleteUser(Long userId);
}