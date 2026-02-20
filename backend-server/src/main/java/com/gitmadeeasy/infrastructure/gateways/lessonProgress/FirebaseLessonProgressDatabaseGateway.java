package com.gitmadeeasy.infrastructure.gateways.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.firebase.FirebaseLessonProgressRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;

import java.util.List;
import java.util.Optional;

public class FirebaseLessonProgressDatabaseGateway implements LessonProgressGateway {
    private final FirebaseLessonProgressRepository firebase;
    private final LessonProgressSchemaMapper mapper;

    public FirebaseLessonProgressDatabaseGateway(FirebaseLessonProgressRepository firebase, LessonProgressSchemaMapper mapper) {
        this.firebase = firebase;
        this.mapper = mapper;
    }

    @Override
    public LessonProgress save(LessonProgress lessonProgress) {
        return this.mapper.fromFirebaseSchema(
                this.firebase.save(this.mapper.toFirebaseSchema(lessonProgress))
        );
    }

    @Override
    public Optional<LessonProgress> findByUserIdAndLessonId(String userId, String lessonId) {
        return this.firebase.findByUserIdAndLessonId(userId, lessonId).map(this.mapper::fromFirebaseSchema);
    }

    @Override
    public List<LessonProgress> findAllByUserId(String userId) {
        return this.firebase.findAllByUserId(userId).stream().map(this.mapper::fromFirebaseSchema).toList();
    }
}