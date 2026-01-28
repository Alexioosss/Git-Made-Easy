package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.users.UserDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;

import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewaysConfiguration {

    @Bean
    public UserGateway userGateway(UserRepository userRepository, UserSchemaMapper userSchemaMapper) {
        return new UserDatabaseGateway(userRepository, userSchemaMapper);
    }
}