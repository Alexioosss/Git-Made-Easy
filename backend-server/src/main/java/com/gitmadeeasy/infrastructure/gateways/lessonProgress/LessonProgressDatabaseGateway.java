package com.gitmadeeasy.infrastructure.gateways.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.LessonProgressRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;

import java.util.Optional;

public class LessonProgressDatabaseGateway implements LessonProgressGateway {
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonProgressSchemaMapper lessonProgressSchemaMapper;

    public LessonProgressDatabaseGateway(LessonProgressRepository lessonProgressRepository,
                                         LessonProgressSchemaMapper lessonProgressSchemaMapper) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonProgressSchemaMapper = lessonProgressSchemaMapper;
    }

    @Override
    public LessonProgress save(LessonProgress lessonProgress) {
        LessonProgressSchema savedSchema = this.lessonProgressRepository.save(
                this.lessonProgressSchemaMapper.toSchema(lessonProgress)
        );
        return this.lessonProgressSchemaMapper.toEntity(savedSchema);
    }

    @Override
    public Optional<LessonProgress> findByUserIdAndLessonId(String userId, String lessonId) {
        return this.lessonProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .map(this.lessonProgressSchemaMapper::toEntity);
    }
}