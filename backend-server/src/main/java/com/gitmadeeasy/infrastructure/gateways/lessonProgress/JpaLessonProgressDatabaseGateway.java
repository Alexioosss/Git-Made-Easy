package com.gitmadeeasy.infrastructure.gateways.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa.JpaLessonProgressRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;

import java.util.List;
import java.util.Optional;

public class JpaLessonProgressDatabaseGateway implements LessonProgressGateway {
    private final JpaLessonProgressRepository jpa;
    private final LessonProgressSchemaMapper lessonProgressSchemaMapper;

    public JpaLessonProgressDatabaseGateway(JpaLessonProgressRepository jpa, LessonProgressSchemaMapper mapper) {
        this.jpa = jpa;
        this.lessonProgressSchemaMapper = mapper;
    }

    @Override
    public LessonProgress save(LessonProgress lessonProgress) {
        return this.lessonProgressSchemaMapper.fromJpaSchema(this.jpa.save(this.lessonProgressSchemaMapper.toJpaSchema(lessonProgress)));
    }

    @Override
    public Optional<LessonProgress> findByUserIdAndLessonId(String userId, String lessonId) {
        return this.jpa.findByUserIdAndLessonId(userId, lessonId).map(this.lessonProgressSchemaMapper::fromJpaSchema);
    }

    @Override
    public List<LessonProgress> findAllByUserId(String userId) {
        return this.jpa.findAllByUserId(userId).stream().map(this.lessonProgressSchemaMapper::fromJpaSchema).toList();
    }
}