package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.LessonProgressRepository;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.firebase.FirebaseLessonProgressRepository;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa.ConcreteJpaLessonProgressRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.firebase.FirebaseLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.ConcreteJpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.firebase.FirebaseTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.ConcreteJpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.firebase.FirebaseTaskRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.ConcreteJpaTaskRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase.FirebaseUserRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.ConcreteJpaUserRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RepositoriesConfiguration {

    // Firebase Repositories (dev profile)
    @Bean
    @Profile("dev")
    public UserRepository firebaseUserRepository(Firestore firestore) {
        return new FirebaseUserRepository(firestore);
    }

    @Bean
    @Profile("dev")
    public LessonRepository firebaseLessonRepository(Firestore firestore) {
        return new FirebaseLessonRepository(firestore);
    }

    @Bean
    @Profile("dev")
    public TaskRepository firebaseTaskRepository(Firestore firestore) {
        return new FirebaseTaskRepository(firestore);
    }

    @Bean
    @Profile("dev")
    public TaskAttemptRepository firebaseTaskAttemptRepository(Firestore firestore) {
        return new FirebaseTaskAttemptRepository(firestore);
    }

    @Bean
    @Profile("dev")
    public LessonProgressRepository firebaseLessonProgressRepository(Firestore firestore) {
        return new FirebaseLessonProgressRepository(firestore);
    }

    // JPA Repositories (test profile)
    @Bean
    @Profile("test")
    public UserRepository jpaUserRepository(com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.AbstractJpaUserRepository abstractJpaUserRepository) {
        return new ConcreteJpaUserRepository(abstractJpaUserRepository);
    }

    @Bean
    @Profile("test")
    public LessonRepository jpaLessonRepository(com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.AbstractJpaLessonRepository abstractJpaLessonRepository) {
        return new ConcreteJpaLessonRepository(abstractJpaLessonRepository);
    }

    @Bean
    @Profile("test")
    public TaskRepository jpaTaskRepository(com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.AbstractJpaTaskRepository abstractJpaTaskRepository) {
        return new ConcreteJpaTaskRepository(abstractJpaTaskRepository);
    }

    @Bean
    @Profile("test")
    public TaskAttemptRepository jpaTaskAttemptRepository(com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.AbstractJpaTaskAttemptRepository abstractJpaTaskAttemptRepository) {
        return new ConcreteJpaTaskAttemptRepository(abstractJpaTaskAttemptRepository);
    }

    @Bean
    @Profile("test")
    public LessonProgressRepository jpaLessonProgressRepository(com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa.AbstractJpaLessonProgressRepository abstractJpaLessonProgressRepository) {
        return new ConcreteJpaLessonProgressRepository(abstractJpaLessonProgressRepository);
    }
}
