package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.AbstractJpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.ConcreteJpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase.FirebaseUserRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.AbstractJpaUserRepository;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.ConcreteJpaUserRepository;
import com.google.cloud.firestore.Firestore;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
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
}