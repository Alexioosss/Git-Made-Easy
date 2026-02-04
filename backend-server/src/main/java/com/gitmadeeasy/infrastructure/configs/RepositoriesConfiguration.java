package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.AbstractJpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.ConcreteJpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.AbstractJpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.ConcreteJpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.AbstractJpaTaskRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.ConcreteJpaTaskRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.AbstractJpaUserRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.ConcreteJpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoriesConfiguration {

//    @Bean
//    public UserRepository userRepository(Firestore firestore) {
//        return new FirebaseUserRepository(firestore);
//    }

    @Bean
    public UserRepository userRepository(AbstractJpaUserRepository jpaUserRepository) {
        return new ConcreteJpaUserRepository(jpaUserRepository);
    }

    @Bean
    public LessonRepository lessonRepository(AbstractJpaLessonRepository jpaLessonRepository) {
        return new ConcreteJpaLessonRepository(jpaLessonRepository);
    }

    @Bean
    public TaskRepository taskRepository(AbstractJpaTaskRepository jpaTaskRepository) {
        return new ConcreteJpaTaskRepository(jpaTaskRepository);
    }

    @Bean
    public TaskAttemptRepository taskAttemptRepository(AbstractJpaTaskAttemptRepository jpaTaskAttemptRepository) {
        return new ConcreteJpaTaskAttemptRepository(jpaTaskAttemptRepository);
    }
}