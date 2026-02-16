package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.firebase.FirebaseLessonProgressRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.firebase.FirebaseLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.firebase.FirebaseTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.firebase.FirebaseTaskRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase.FirebaseUserRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import com.google.cloud.firestore.Firestore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration @Profile("dev")
public class RepositoriesConfiguration {

    // Firebase Repositories (dev profile)
    @Bean @Profile("dev")
    public FirebaseUserRepository firebaseUserRepository(Firestore firestore) {
        return new FirebaseUserRepository(firestore);
    }

    @Bean @Profile("dev")
    public FirebaseLessonRepository firebaseLessonRepository(Firestore firestore, LessonSchemaMapper mapper) {
        return new FirebaseLessonRepository(firestore);
    }

    @Bean @Profile("dev")
    public FirebaseTaskRepository firebaseTaskRepository(Firestore firestore, TaskSchemaMapper taskSchemaMapper) {
        return new FirebaseTaskRepository(firestore);
    }

    @Bean @Profile("dev")
    public FirebaseTaskAttemptRepository firebaseTaskAttemptRepository(Firestore firestore, TaskAttemptSchemaMapper mapper) {
        return new FirebaseTaskAttemptRepository(firestore);
    }

    @Bean @Profile("dev")
    public FirebaseLessonProgressRepository firebaseLessonProgressRepository(Firestore firestore, LessonProgressSchemaMapper mapper) {
        return new FirebaseLessonProgressRepository(firestore);
    }
}