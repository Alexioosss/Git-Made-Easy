package com.gitmadeeasy.unit.usecases.tasks;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.tasks.GetTasksForLesson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetTasksForLessonTest {
    @Mock private TaskGateway taskGateway;
    @InjectMocks private GetTasksForLesson getTasksForLesson;


    @Test
    @DisplayName("Get Tasks For Lesson - Tasks Exist")
    void execute_WhenTasksExistForLesson_ReturnsTasksList() {
        // Arrange
        Lesson lesson1 = new Lesson("1", "Intro", "Description", DifficultyLevels.EASY, 1);
        List<Task> tasksList = List.of(
                new Task("1", "1",
                        "first git task",
                        "Let's start this journey, shall we?",
                        "git start",
                        "easier than it may seem...", 1, DifficultyLevels.EASY));
        lesson1.setTasks(tasksList);
        when(this.taskGateway.getTasksByLessonId("1")).thenReturn(tasksList);

        // Act
        List<Task> retrievedTasks = this.getTasksForLesson.execute("1");

        // Assert
        assertEquals(1, retrievedTasks.size());
        assertEquals("1", retrievedTasks.get(0).getLessonId());
        assertEquals("1", retrievedTasks.get(0).getTaskId());
        assertEquals(1, retrievedTasks.get(0).getTaskOrder());
    }

    @Test
    @DisplayName("Get Tasks For Lesson - No Tasks Exist")
    void execute_WhenNoTasksExistForLesson_ReturnsEmptyTasksList() {
        // Arrange
        when(this.taskGateway.getTasksByLessonId("1")).thenReturn(List.of());

        // Act
        List<Task> emptyTasksList = this.getTasksForLesson.execute("1");

        // Assert
        assertEquals(0, emptyTasksList.size());
    }
}