package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.FirebaseLessonProgressDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.firebase.FirebaseLessonProgressRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.FirebaseLessonDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.firebase.FirebaseLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.FirebaseTaskAttemptDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.firebase.FirebaseTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.FirebaseTaskDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.firebase.FirebaseTaskRepository;
import com.gitmadeeasy.infrastructure.gateways.users.FirebaseUserDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase.FirebaseUserRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration @Profile("dev")
public class FirebaseGatewaysConfiguration {

    @Bean
    public UserGateway userGateway(FirebaseUserRepository firebase, UserSchemaMapper mapper) {
        return new FirebaseUserDatabaseGateway(firebase, mapper);
    }

    @Bean
    public LessonGateway lessonGateway(FirebaseLessonRepository firebase, LessonSchemaMapper mapper) {
        return new FirebaseLessonDatabaseGateway(firebase, mapper);
    }

    @Bean
    public TaskGateway taskGateway(FirebaseTaskRepository firebase, TaskSchemaMapper mapper) {
        return new FirebaseTaskDatabaseGateway(firebase, mapper);
    }

    @Bean
    public TaskAttemptGateway taskAttemptGateway(FirebaseTaskAttemptRepository firebase,
                                                 TaskAttemptSchemaMapper mapper) {
        return new FirebaseTaskAttemptDatabaseGateway(firebase, mapper);
    }

    @Bean
    public LessonProgressGateway lessonProgressGateway(FirebaseLessonProgressRepository firebase,
                                                       LessonProgressSchemaMapper mapper) {
        return new FirebaseLessonProgressDatabaseGateway(firebase, mapper);
    }
}