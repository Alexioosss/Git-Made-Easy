package com.gitmadeeasy.testConfig;

import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

/**
 * Test-only Configuration class to act as a mock implementation of the {@link UserIdentityProvider} class
 * Used mainly in integration tests, and only when the 'test' spring profile is active.
 * This configuration class replaces the real {@link UserIdentityProvider}, which uses Firebase.
 * These operations make external calls and real credentials, thus not suitable for automated tests.
 */
@Configuration @Profile("test")
public class FirebaseTestConfig {

    @Bean
    public UserIdentityProvider userIdentityProvider() {
        return new UserIdentityProvider() {
            @Override
            public String createUser(String firstName, String lastName, String email, String password) {
                return UUID.randomUUID().toString();
            }

            @Override
            public String sendVerificationEmail(String emailAddress) {
                return "verification-email@gmail.com";
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
}