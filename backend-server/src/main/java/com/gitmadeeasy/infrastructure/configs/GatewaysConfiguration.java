package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.factories.users.UserFactory;
import com.gitmadeeasy.infrastructure.gateways.users.UserDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewaysConfiguration {

    UserGateway userGateway(UserRepository userRepository, UserFactory userFactory) {
        return new UserDatabaseGateway(userRepository, userFactory);
    }
}