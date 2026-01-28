package com.gitmadeeasy.infrastructure.factories.users;

import com.gitmadeeasy.entities.users.User;

public interface UserFactory {
    User createUserFromRequest(String firstName, String lastName, String emailAddress);
}