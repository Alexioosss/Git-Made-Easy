package com.gitmadeeasy.testConfig;

import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

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