package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;

@Configuration
public class MappersConfiguration {
    
    @Bean
    public UserSchemaMapper userSchemaMapper() {
        return new UserSchemaMapper();
    }

    @Bean
    public LessonSchemaMapper lessonSchemaMapper() { return new LessonSchemaMapper(); }

    @Bean
    public TaskSchemaMapper taskSchemaMapper() { return new TaskSchemaMapper(); }
}