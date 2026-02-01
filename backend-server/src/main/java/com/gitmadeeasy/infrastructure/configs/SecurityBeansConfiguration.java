package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.infrastructure.security.BCryptPasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeansConfiguration {

    @Bean
    public PasswordHasher passwordHasher() { return new BCryptPasswordHasher(); }
}