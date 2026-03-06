package com.gitmadeeasy.unit.usecases.dashboard;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.dashboard.DashboardResponse;
import com.gitmadeeasy.usecases.dashboard.GetDashboardData;
import com.gitmadeeasy.usecases.dashboard.LessonSummary;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetDashboardDataTest {
    @Mock private UserGateway userGateway;
    @Mock private LessonGateway lessonGateway;
    @Mock private LessonProgressGateway lessonProgressGateway;
    @Mock private TaskAttemptGateway taskAttemptGateway;
    @InjectMocks private GetDashboardData getDashboardData;

    private static final String USER_ID = "user1";


    @Test
    @DisplayName("Execute - User Not Found - Throws UserNotFoundWithIdException")
    void execute_WhenUserNotFound_ThrowsException() {
        // Arrange
        when(this.userGateway.getUserById(USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundWithIdException.class, () -> this.getDashboardData.execute(USER_ID));
    }

    @Test
    @DisplayName("Get Dashboard – Valid Data – Returns DashboardResponse")
    void execute_WhenValidData_ReturnsDashboardResponse() {
        // Arrange
        User user = provideUser();
        Lesson lesson1 = provideLesson("L1", 2);
        Lesson lesson2 = provideLesson("L2", 3);

        var progress1 = new LessonProgress(USER_ID, "L1", 1, 2);
        var taskProgress = List.of(new TaskProgress("TP1", USER_ID, "T1"));

        when(this.userGateway.getUserById(USER_ID)).thenReturn(Optional.of(user));
        when(this.lessonGateway.findAllLessons()).thenReturn(List.of(lesson1, lesson2));
        when(this.lessonProgressGateway.findAllByUserId(USER_ID)).thenReturn(List.of(progress1));
        when(this.taskAttemptGateway.findAllByUserId(USER_ID)).thenReturn(taskProgress);

        // Act
        DashboardResponse response = this.getDashboardData.execute(USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(USER_ID, response.userId());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals(2, response.lessons().size());
        assertEquals(taskProgress, response.tasksProgress());

        LessonSummary summary1 = response.lessons().stream()
                .filter(s -> s.lessonId().equals("L1")).findFirst().orElseThrow();
        assertEquals("L1", summary1.lessonId());
        assertEquals(1, summary1.completedTasksCount());
        assertEquals(2, summary1.totalTasksCount());

        LessonSummary summary2 = response.lessons().stream()
                .filter(s -> s.lessonId().equals("L2")).findFirst().orElseThrow();
        assertEquals("L2", summary2.lessonId());
        assertEquals(0, summary2.completedTasksCount());
        assertEquals(3, summary2.totalTasksCount());
    }

    @Test
    @DisplayName("Get Dashboard – No Lessons – Returns Empty Lesson Summaries")
    void execute_WhenNoLessons_ReturnsEmptyList() {
        // Arrange
        when(this.userGateway.getUserById(USER_ID)).thenReturn(Optional.of(provideUser()));
        when(this.lessonGateway.findAllLessons()).thenReturn(List.of());
        when(this.lessonProgressGateway.findAllByUserId(USER_ID)).thenReturn(List.of());
        when(this.taskAttemptGateway.findAllByUserId(USER_ID)).thenReturn(List.of());

        // Act
        DashboardResponse response = this.getDashboardData.execute(USER_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.lessons().isEmpty(), "Lessons should be empty");
        assertTrue(response.tasksProgress().isEmpty(), "Task progress should be empty");
    }

    @Test
    @DisplayName("Get Dashboard – No Lesson Progress – Defaults To Zero Progress")
    void execute_WhenNoProgress_ReturnsDefaultProgress() {
        // Arrange
        User user = provideUser();
        Lesson lesson = provideLesson("L1", 4); // 4 tasks in this lesson

        when(this.userGateway.getUserById(USER_ID)).thenReturn(Optional.of(user));
        when(this.lessonGateway.findAllLessons()).thenReturn(List.of(lesson));
        when(this.lessonProgressGateway.findAllByUserId(USER_ID)).thenReturn(List.of());
        when(this.taskAttemptGateway.findAllByUserId(USER_ID)).thenReturn(List.of());

        // Act
        DashboardResponse response = this.getDashboardData.execute(USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.lessons().size());

        LessonSummary summary = response.lessons().get(0);
        assertEquals("L1", summary.lessonId());
        assertEquals(0, summary.completedTasksCount(), "Completed tasks should default to 0");
        assertEquals(4, summary.totalTasksCount(), "Total tasks should match lesson task count");
    }




    // ----- HELPER METHODS ----- //

    private static User provideUser() {
        return new User(USER_ID, "John", "Doe", "myemail1@gmail.com");
    }

    private static Lesson provideLesson(String id, int taskCount) {
        Lesson lesson = new Lesson(
                id, "Lesson " + id,
                "Description", DifficultyLevels.EASY,  1);

        List<Task> tasks = new ArrayList<>();
        List<String> taskIds = new ArrayList<>();

        for(int i = 0; i < taskCount; i++) {
            String taskId = "T" + i;
            tasks.add(new Task(
                    taskId, id,"Task " + i,
                    "Content", "expCommand", "hint",
                    1, DifficultyLevels.EASY));
            taskIds.add(taskId);
        }
        lesson.setTasks(tasks);
        lesson.setTaskIds(taskIds);
        return lesson;
    }
}