package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public TaskAttemptSchemaMapper taskAttemptSchemaMapper() { return new TaskAttemptSchemaMapper(); }

    @Bean
    public LessonProgressSchemaMapper lessonProgressSchemaMapper() { return new LessonProgressSchemaMapper(); }
}