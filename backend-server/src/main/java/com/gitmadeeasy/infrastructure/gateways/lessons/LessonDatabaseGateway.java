package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;

import java.util.Optional;

public class LessonDatabaseGateway implements LessonGateway {
    private final LessonRepository lessonRepository;
    private final LessonSchemaMapper lessonSchemaMapper;

    public LessonDatabaseGateway(LessonRepository lessonRepository, LessonSchemaMapper lessonSchemaMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonSchemaMapper = lessonSchemaMapper;
    }

    @Override
    public Lesson createLesson(Lesson newLesson) {
        LessonSchema savedLessonSchema = this.lessonRepository.save(
                this.lessonSchemaMapper.toSchema(newLesson)
        );
        return this.lessonSchemaMapper.toEntity(savedLessonSchema);
    }

    @Override
    public Optional<Lesson> getLessonById(String lessonId) {
        return this.lessonRepository.findById(lessonId)
                .map(this.lessonSchemaMapper::toEntity);
    }

    @Override
    public boolean existsById(String lessonId) {
        return this.lessonRepository.existsById(lessonId);
    }
}