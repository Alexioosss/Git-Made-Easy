package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.infrastructure.auth.firebase.FirebaseUserIdentityProvider;
import com.gitmadeeasy.infrastructure.gateways.security.JwtAuthenticationFilter;
import com.gitmadeeasy.infrastructure.gateways.security.TokenDatabaseGateway;
import com.gitmadeeasy.infrastructure.security.BCryptPasswordHasher;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeansConfiguration {

    @Bean
    public TokenGateway tokenGateway(@Value("${jwt.secret}") String secret) {
        return new TokenDatabaseGateway(secret);
    }

    @Bean
    public PasswordHasher passwordHasher() { return new BCryptPasswordHasher(); }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenGateway tokenGateway) {
        return new JwtAuthenticationFilter(tokenGateway);
    }

    @Bean
    public UserIdentityProvider userIdentityProvider() { return new FirebaseUserIdentityProvider(); }
}