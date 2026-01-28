package com.gitmadeeasy.infrastructure.factories.users;

import com.gitmadeeasy.entities.users.User;

public class UserFactoryImplementation implements UserFactory {

    @Override
    public User createUserFromRequest(String firstName, String lastName, String emailAddress) {
        return new User(
                firstName,
                lastName,
                emailAddress
        );
    }
}