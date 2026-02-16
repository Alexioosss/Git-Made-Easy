package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.JpaLessonProgressDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa.JpaLessonProgressRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.JpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.JpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.JpaTaskRepository;
import com.gitmadeeasy.infrastructure.gateways.users.JpaUserDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.JpaUserRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration @Profile("test")
public class JpaGatewaysConfiguration {

    @Bean
    public UserGateway userGateway(JpaUserRepository jpaUserRepository, UserSchemaMapper userSchemaMapper) {
        return new JpaUserDatabaseGateway(jpaUserRepository, userSchemaMapper);
    }

    @Bean
    public LessonGateway lessonGateway(JpaLessonRepository lessonRepository, LessonSchemaMapper lessonSchemaMapper) {
        return new JpaLessonDatabaseGateway(lessonRepository, lessonSchemaMapper);
    }

    @Bean
    public TaskGateway taskGateway(JpaTaskRepository jpaTaskRepository, TaskSchemaMapper taskSchemaMapper) {
        return new JpaTaskDatabaseGateway(jpaTaskRepository, taskSchemaMapper);
    }

    @Bean
    public TaskAttemptGateway taskAttemptGateway(JpaTaskAttemptRepository taskAttemptRepository,
                                                 JpaTaskRepository taskRepository,
                                                 TaskAttemptSchemaMapper taskAttemptSchemaMapper) {
        return new JpaTaskAttemptDatabaseGateway(taskAttemptRepository, taskRepository, taskAttemptSchemaMapper);
    }

    @Bean
    public LessonProgressGateway lessonProgressGateway(JpaLessonProgressRepository lessonProgressRepository,
                                                       LessonProgressSchemaMapper lessonProgressSchemaMapper) {
        return new JpaLessonProgressDatabaseGateway(lessonProgressRepository, lessonProgressSchemaMapper);
    }
}