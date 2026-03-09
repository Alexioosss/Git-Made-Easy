package com.gitmadeeasy.testConfig;

import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.google.firebase.FirebaseApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

import static org.mockito.Mockito.mock;

@Configuration @Profile("test")
public class TestConfig {

    @Bean @Primary
    public UserIdentityProvider userIdentityProvider() {
        return new UserIdentityProvider() {
            @Override
            public String createUser(String firstName, String lastName, String email, String password) {
                return UUID.randomUUID().toString();
            }

            @Override
            public boolean isEmailVerified(String userId) {
                return true;
            }

            @Override
            public String login(String emailAddress, String password) {
                return "firebase-" + emailAddress;
            }
        };
    }

    @Bean @Primary
    public FirebaseApp firebaseApp() {
        return mock(FirebaseApp.class);
    }
}