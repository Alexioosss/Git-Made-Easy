package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.infrastructure.gateways.security.JwtAuthenticationFilter;
import com.gitmadeeasy.infrastructure.gateways.security.TokenDatabaseGateway;
import com.gitmadeeasy.infrastructure.security.BCryptPasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeansConfiguration {

    @Bean
    public PasswordHasher passwordHasher() { return new BCryptPasswordHasher(); }

    @Bean
    public TokenGateway tokenGateway() { return new TokenDatabaseGateway(); }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenGateway tokenGateway) {
        return new JwtAuthenticationFilter(tokenGateway);
    }
}