package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.enums.TaskCompletionStatus;
import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.JpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.security.UserPrincipal;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.JpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.JpaTaskRepository;
import com.gitmadeeasy.usecases.taskProgress.GetAllTaskProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskProgressControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private GetAllTaskProgress getAllTaskProgress;
    @Autowired private JpaLessonRepository jpaLessonRepository;
    @Autowired private JpaTaskRepository jpaTaskRepository;
    @Autowired private JpaTaskAttemptRepository jpaTaskAttemptRepository;

    private static final String USER_ID = "1";

    @BeforeEach
    void setUp() {
        this.jpaTaskAttemptRepository.deleteAll();
        this.jpaLessonRepository.deleteAll();

        JpaLessonSchema lesson1 = new JpaLessonSchema("Lesson 1", "Description", DifficultyLevels.EASY, 1);
        this.jpaLessonRepository.save(lesson1);

        JpaTaskSchema task1 = new JpaTaskSchema("Title", "Description", "git init",
                "Command to initialize a new git repository", 1, DifficultyLevels.EASY);
        JpaTaskSchema task2 = new JpaTaskSchema("Title", "Description", "git --version",
                "Command to check current git version", 2, DifficultyLevels.EASY);
        JpaTaskSchema task3 = new JpaTaskSchema("Title", "Description", "git status",
                "Command to check changes and history of the repository", 3, DifficultyLevels.EASY);
        task1 = this.jpaTaskRepository.save(task1);
        task2 = this.jpaTaskRepository.save(task2);
        task3 = this.jpaTaskRepository.save(task3);

        JpaTaskAttemptSchema p1 = new JpaTaskAttemptSchema("1", TaskCompletionStatus.IN_PROGRESS, 1,
                "git initialize", "", LocalDate.now().minusDays(1), LocalDate.now(), task1);
        JpaTaskAttemptSchema p2 = new JpaTaskAttemptSchema("1", TaskCompletionStatus.IN_PROGRESS, 1,
                "git version", "", LocalDate.now().minusDays(1), LocalDate.now(), task2);
        JpaTaskAttemptSchema p3 = new JpaTaskAttemptSchema("1", TaskCompletionStatus.IN_PROGRESS, 1,
                "git history", "", LocalDate.now().minusDays(1), LocalDate.now(), task3);
        this.jpaTaskAttemptRepository.save(p1);
        this.jpaTaskAttemptRepository.save(p2);
        this.jpaTaskAttemptRepository.save(p3);
    }


    @Test
    @DisplayName("Get All Task Progress - Authenticated User Has Task Progresses")
    void getAllTaskProgress_WhenAuthenticatedUserHasTaskProgresses_ReturnsTaskProgressList() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/tasks/progress")
                        .with(user(new UserPrincipal(USER_ID))))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].taskId").exists())
                .andExpect(jsonPath("$.[1].taskId").exists())
                .andExpect(jsonPath("$.[2].taskId").exists());
    }
}