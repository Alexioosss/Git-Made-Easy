package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.CreateUser;

import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.GetUserById;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfiguration {

    @Bean
    public CreateUser createUserUseCase(UserGateway userGateway, PasswordHasher passwordHasher) {
        return new CreateUser(userGateway, passwordHasher);
    }

    @Bean
    public GetUserById getUserById(UserGateway userGateway) {
        return new GetUserById(userGateway);
    }

    @Bean
    public GetUserByEmail getUserByEmail(UserGateway userGateway) { return new GetUserByEmail(userGateway); }
}