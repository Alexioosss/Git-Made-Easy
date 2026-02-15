package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.attemptTask.AttemptTask;
import com.gitmadeeasy.usecases.attemptTask.GetTaskProgress;
import com.gitmadeeasy.usecases.auth.*;
import com.gitmadeeasy.usecases.lessonProgress.GetLessonProgress;
import com.gitmadeeasy.usecases.lessonProgress.UpdateLessonProgress;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.GetTaskById;
import com.gitmadeeasy.usecases.users.CreateUser;
import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.GetUserById;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfiguration {

    // ----- User-Related Use Cases ----- //

    @Bean
    public CreateUser createUserUseCase(UserGateway userGateway, UserIdentityProvider identityProvider,
                                        PasswordHasher passwordHasher, EmailSender emailSender) {
        return new CreateUser(userGateway, identityProvider, passwordHasher, emailSender);
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
    public LoginUser loginUser(UserGateway userGateway, TokenGateway tokenGateway) {
        return new LoginUser(userGateway, tokenGateway);
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



    // ----- Task-Related Use Cases ----- //

    @Bean
    public CreateTask createTask(TaskGateway taskGateway,LessonGateway lessonGateway) {
        return new CreateTask(taskGateway, lessonGateway);
    }

    @Bean
    public GetTaskById getTaskById(TaskGateway taskGateway, LessonGateway lessonGateway) {
        return new GetTaskById(taskGateway, lessonGateway);
    }



    // ----- Task Progress-Related Use Cases ----- //

    @Bean
    public AttemptTask taskAttempt(TaskAttemptGateway taskAttemptGateway,
                                   TaskGateway taskGateway,
                                   UpdateLessonProgress updateLessonProgress) {
        return new AttemptTask(taskAttemptGateway, taskGateway, updateLessonProgress);
    }

    @Bean
    public GetTaskProgress getTaskProgress(TaskAttemptGateway taskAttemptGateway, LessonGateway lessonGateway) {
        return new GetTaskProgress(taskAttemptGateway, lessonGateway);
    }



    // ----- Lesson Progress-Related Use Cases ----- //

    @Bean
    public GetLessonProgress getLessonProgress(LessonProgressGateway lessonProgressGateway) {
        return new GetLessonProgress(lessonProgressGateway);
    }

    @Bean
    public UpdateLessonProgress updateLessonProgress(LessonProgressGateway lessonProgressGateway,
                                                     TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway) {
        return new UpdateLessonProgress(lessonProgressGateway, taskAttemptGateway, taskGateway);
    }
}