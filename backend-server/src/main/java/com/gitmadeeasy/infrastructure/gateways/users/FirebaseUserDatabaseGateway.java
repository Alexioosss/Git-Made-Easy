package com.gitmadeeasy.infrastructure.gateways.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase.FirebaseUserRepository;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;

import java.util.Optional;

public class FirebaseUserDatabaseGateway implements UserGateway {
    private final FirebaseUserRepository firebase;
    private final UserSchemaMapper mapper;

    public FirebaseUserDatabaseGateway(FirebaseUserRepository firebase, UserSchemaMapper mapper) {
        this.firebase = firebase;
        this.mapper = mapper;
    }

    @Override
    public User createUser(User newUser, String hashedPassword) {
        return this.mapper.fromFirebaseSchema(
                this.firebase.save(this.mapper.toFirebaseSchema(newUser, hashedPassword))
        );
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return this.firebase.findById(userId).map(this.mapper::fromFirebaseSchema);
    }

    @Override
    public Optional<User> getUserByEmailAddress(String emailAddress) {
        return this.firebase.findByEmailAddress(emailAddress).map(this.mapper::fromFirebaseSchema);
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        return this.firebase.existsByEmailAddress(emailAddress);
    }
}