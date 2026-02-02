package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.TaskDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.gitmadeeasy.infrastructure.gateways.users.UserDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;

import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewaysConfiguration {

    @Bean
    public UserGateway userGateway(UserRepository userRepository, UserSchemaMapper userSchemaMapper) {
        return new UserDatabaseGateway(userRepository, userSchemaMapper);
    }

    @Bean
    public LessonGateway lessonGateway(LessonRepository lessonRepository, LessonSchemaMapper lessonSchemaMapper) {
        return new LessonDatabaseGateway(lessonRepository, lessonSchemaMapper);
    }

    @Bean
    public TaskGateway taskGateway(TaskRepository taskRepository, TaskSchemaMapper taskSchemaMapper) {
        return new TaskDatabaseGateway(taskRepository, taskSchemaMapper);
    }
}