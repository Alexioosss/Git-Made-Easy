package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.users.CreateUser;

import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.GetUserById;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfiguration {

    // ----- User-Related Use Cases ----- //

    @Bean
    public CreateUser createUserUseCase(UserGateway userGateway, PasswordHasher passwordHasher) {
        return new CreateUser(userGateway, passwordHasher);
    }

    @Bean
    public GetUserById getUserById(UserGateway userGateway) {
        return new GetUserById(userGateway);
    }

    @Bean
    public GetUserByEmail getUserByEmail(UserGateway userGateway) { return new GetUserByEmail(userGateway); }


    // ----- Auth-Related Use Cases ----- //

    @Bean
    public LoginUser loginUser(UserGateway userGateway, PasswordHasher passwordHasher, TokenGateway tokenGateway) {
        return new LoginUser(userGateway, passwordHasher, tokenGateway);
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
    public GetLessonById getLessonById(LessonGateway lessonGateway) {
        return new GetLessonById(lessonGateway);
    }
}