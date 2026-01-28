package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.factories.users.UserFactory;
import com.gitmadeeasy.usecases.users.CreateUserUseCase;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfiguration {

    CreateUserUseCase createUserUseCase(UserGateway userGateway, UserFactory userFactory) {
        return new CreateUserUseCase(userGateway, userFactory);
    }
}