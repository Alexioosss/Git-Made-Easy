package com.gitmadeeasy.usecases.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GetLessonProgress {
    private final LessonProgressGateway lessonProgressGateway;
    private static final Logger log = LoggerFactory.getLogger(GetLessonProgress.class);

    public GetLessonProgress(LessonProgressGateway lessonProgressGateway) {
        this.lessonProgressGateway = lessonProgressGateway;
    }

    public Optional<LessonProgress> execute(String userId, String lessonId) {
        log.info("Fetching lesson progress by userID= {} and lessonID= {}", userId, lessonId);
        return this.lessonProgressGateway.findByUserIdAndLessonId(userId, lessonId);
    }
}