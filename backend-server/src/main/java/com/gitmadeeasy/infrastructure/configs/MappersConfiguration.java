package com.gitmadeeasy.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapperImplementation;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapperImplementation;

@Configuration
public class MappersConfiguration {
    
    @Bean
    public UserSchemaMapper userSchemaMapper() {
        return new UserSchemaMapperImplementation();
    }

    @Bean
    public UserResponseMapperImplementation userResponseMapper() {
        return new UserResponseMapperImplementation();
    }
}