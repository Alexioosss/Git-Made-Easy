package com.gitmadeeasy.usecases.dashboard;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetDashboardData {
    private final UserGateway userGateway;
    private final LessonProgressGateway lessonProgressGateway;
    private final TaskAttemptGateway taskAttemptGateway;
    private static final Logger log = LoggerFactory.getLogger(GetDashboardData.class);

    public GetDashboardData(UserGateway userGateway, LessonProgressGateway lessonProgressGateway,
                            TaskAttemptGateway taskAttemptGateway) {
        this.userGateway = userGateway;
        this.lessonProgressGateway = lessonProgressGateway;
        this.taskAttemptGateway = taskAttemptGateway;
    }

    public DashboardResponse execute(String userId) {
        log.info("Loading all data for user {}", userId);

        User user = this.userGateway.getUserById(userId).orElseThrow(() -> new UserNotFoundWithIdException(userId));
        log.info("User found. UserID {}, email address {}", user.getUserId(), user.getEmailAddress());

        List<LessonProgress> lessonsProgress = this.lessonProgressGateway.findAllByUserId(userId);
        log.info("Lessons progress found. Progress found: {}", lessonsProgress.size());

        List<TaskProgress> tasksProgress = this.taskAttemptGateway.findAllByUserId(userId);
        log.info("Tasks progress found. Progress found: {}", tasksProgress.size());

        return new DashboardResponse(
                user.getUserId(), user.getFirstName(), user.getLastName(), lessonsProgress, tasksProgress);
    }
}