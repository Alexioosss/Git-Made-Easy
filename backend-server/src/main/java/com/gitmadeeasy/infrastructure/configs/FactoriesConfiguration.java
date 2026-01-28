package com.gitmadeeasy.infrastructure.configs;

import org.springframework.context.annotation.Configuration;

import com.gitmadeeasy.infrastructure.factories.users.UserFactory;
import com.gitmadeeasy.infrastructure.factories.users.UserFactoryImplementation;

@Configuration
public class FactoriesConfiguration {

    UserFactory userFactory() { return new UserFactoryImplementation(); }
}