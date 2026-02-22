package com.gitmadeeasy.usecases.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class GetAllLessonProgress {
    private final LessonProgressGateway lessonProgressGateway;
    private final LessonGateway lessonGateway;
    private static final Logger log = LoggerFactory.getLogger(GetAllLessonProgress.class);

    public GetAllLessonProgress(LessonProgressGateway lessonProgressGateway, LessonGateway lessonGateway) {
        this.lessonProgressGateway = lessonProgressGateway;
        this.lessonGateway = lessonGateway;
    }

    public List<LessonProgress> execute(String userId) {
        log.info("Fetching all lesson progress for userID={}", userId);

        // Find all lesson progress for the user
        List<LessonProgress> progressList = this.lessonProgressGateway.findAllByUserId(userId);

        // For each lesson progress, find the lesson, and sort the progressList by lessonOrder
        return progressList.stream().sorted(Comparator.comparing(p ->
                this.lessonGateway.getLessonById(p.getLessonId())
                        .map(Lesson::getLessonOrder).orElse(Integer.MAX_VALUE))).toList();
    }
}