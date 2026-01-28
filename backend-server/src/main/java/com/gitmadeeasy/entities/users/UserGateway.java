package com.gitmadeeasy.entities.users;

import java.util.Optional;

public interface UserGateway {
    User createUser(User user);
    Optional<User> getUserById(String userId);
    Optional<User> getUserByEmailAddress(String emailAddress);
}