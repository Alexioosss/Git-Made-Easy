package com.gitmadeeasy.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;

@Configuration
public class MappersConfiguration {
    
    @Bean
    public UserSchemaMapper userSchemaMapper() {
        return new UserSchemaMapper();
    }

    @Bean
    public UserResponseMapper userResponseMapper() {
        return new UserResponseMapper();
    }
}