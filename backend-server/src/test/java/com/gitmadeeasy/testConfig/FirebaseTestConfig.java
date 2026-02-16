package com.gitmadeeasy.testConfig;

import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class FirebaseTestConfig {

    @Bean
    public UserIdentityProvider userIdentityProvider() {
        return new UserIdentityProvider() {
            @Override
            public String createUser(String firstName, String lastName, String email, String password) {
                return "test-firebase-id";
            }

            @Override
            public String generateEmailVerificationLink(String email) {
                return "http://email-test-link";
            }

            @Override
            public boolean isEmailVerified(String userId) {
                return true;
            }
        };
    }
}