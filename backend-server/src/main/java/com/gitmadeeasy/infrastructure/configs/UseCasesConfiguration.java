package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.*;
import com.gitmadeeasy.usecases.dashboard.GetDashboardData;
import com.gitmadeeasy.usecases.lessonProgress.GetAllLessonProgress;
import com.gitmadeeasy.usecases.lessonProgress.GetLessonProgress;
import com.gitmadeeasy.usecases.lessonProgress.LessonProgressFacade;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetAllLessons;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.GetNextLesson;
import com.gitmadeeasy.usecases.taskProgress.AttemptTask;
import com.gitmadeeasy.usecases.taskProgress.GetAllTaskProgress;
import com.gitmadeeasy.usecases.taskProgress.GetTaskProgress;
import com.gitmadeeasy.usecases.taskProgress.SyncTaskProgress;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.GetTaskById;
import com.gitmadeeasy.usecases.tasks.GetTasksForLesson;
import com.gitmadeeasy.usecases.users.CreateUser;
import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.GetUserById;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfiguration {

    // ----- User-Related Use Cases ----- //

    @Bean
    public CreateUser createUser(UserGateway userGateway, UserIdentityProvider identityProvider) {
        return new CreateUser(userGateway, identityProvider);
    }

    @Bean
    public GetUserById getUserById(UserGateway userGateway, UserIdentityProvider identityProvider) {
        return new GetUserById(userGateway, identityProvider);
    }

    @Bean
    public GetUserByEmail getUserByEmail(UserGateway userGateway, UserIdentityProvider identityProvider) {
        return new GetUserByEmail(userGateway, identityProvider);
    }



    // ----- Auth-Related Use Cases ----- //

    @Bean
    public LoginUser loginUser(UserGateway userGateway, TokenGateway tokenGateway, UserIdentityProvider identityProvider) {
        return new LoginUser(userGateway, tokenGateway, identityProvider);
    }

    @Bean
    public LogoutUser logoutUser(TokenGateway tokenGateway) { return new LogoutUser(tokenGateway); }

    @Bean
    public RefreshToken refreshToken(UserGateway userGateway, TokenGateway tokenGateway) {
        return new RefreshToken(userGateway, tokenGateway);
    }



    // ----- Lesson-Related Use Cases ----- //

    @Bean
    public CreateLesson createLesson(LessonGateway lessonGateway) {
        return new CreateLesson(lessonGateway);
    }

    @Bean
    public GetLessonById getLessonById(LessonGateway lessonGateway, TaskGateway taskGateway) {
        return new GetLessonById(lessonGateway, taskGateway);
    }

    @Bean
    public GetAllLessons getAllLessons(LessonGateway lessonGateway, TaskGateway taskGateway) {
        return new GetAllLessons(lessonGateway, taskGateway);
    }

    @Bean
    public GetNextLesson getNextLesson(LessonGateway lessonGateway) { return new GetNextLesson(lessonGateway); }



    // ----- Task-Related Use Cases ----- //

    @Bean
    public CreateTask createTask(TaskGateway taskGateway,LessonGateway lessonGateway) {
        return new CreateTask(taskGateway, lessonGateway);
    }

    @Bean
    public GetTaskById getTaskById(TaskGateway taskGateway, LessonGateway lessonGateway) {
        return new GetTaskById(taskGateway, lessonGateway);
    }

    @Bean
    public GetTasksForLesson getTasksForLesson(TaskGateway taskGateway) {
        return new GetTasksForLesson(taskGateway);
    }



    // ----- Task Progress-Related Use Cases ----- //

    @Bean
    public AttemptTask taskAttempt(TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway,
                                   LessonProgressFacade lessonProgressFacade) {
        return new AttemptTask(taskAttemptGateway, taskGateway, lessonProgressFacade);
    }

    @Bean
    public GetTaskProgress getTaskProgress(TaskAttemptGateway taskAttemptGateway, LessonGateway lessonGateway) {
        return new GetTaskProgress(taskAttemptGateway, lessonGateway);
    }

    @Bean
    public SyncTaskProgress syncTaskProgress(TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway,
                                             LessonProgressFacade lessonGateway) {
        return new SyncTaskProgress(taskAttemptGateway, taskGateway, lessonGateway);
    }

    @Bean
    public GetAllTaskProgress getAllTaskProgress(TaskAttemptGateway taskAttemptGateway) {
        return new GetAllTaskProgress(taskAttemptGateway);
    }



    // ----- Lesson Progress-Related Use Cases ----- //

    @Bean
    public GetLessonProgress getLessonProgress(LessonProgressGateway lessonProgressGateway) {
        return new GetLessonProgress(lessonProgressGateway);
    }

    @Bean
    public GetAllLessonProgress getAllLessonProgress(LessonProgressGateway lessonProgressGateway, LessonGateway lessonGateway) {
        return new GetAllLessonProgress(lessonProgressGateway,lessonGateway);
    }

    @Bean
    public LessonProgressFacade updateLessonProgress(LessonProgressGateway lessonProgressGateway,
                                                     TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway) {
        return new LessonProgressFacade(lessonProgressGateway, taskAttemptGateway, taskGateway);
    }



    // ----- Dashboard-Related Use Cases ----- //

    @Bean
    public GetDashboardData getDashboardData(UserGateway userGateway, LessonGateway lessonGateway,
                                             LessonProgressGateway lessonProgressGateway, TaskAttemptGateway taskAttemptGateway) {
        return new GetDashboardData(userGateway, lessonGateway, lessonProgressGateway, taskAttemptGateway);
    }
}