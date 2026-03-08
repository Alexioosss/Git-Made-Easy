package com.gitmadeeasy.unit.usecases.taskProgress;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.taskProgress.GetAllTaskProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.gitmadeeasy.testUtil.TaskTestDataFactory.taskProgressesList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllTaskProgressTest {
    @Mock private TaskAttemptGateway taskAttemptGateway;
    @InjectMocks private GetAllTaskProgress getAllTaskProgress;


    @Test
    @DisplayName("Get All Task Progress - Task Progresses Exist")
    void execute_WhenTaskProgressesExist_ReturnsTaskProgressList() {
        // Arrange
        List<TaskProgress> taskProgresses = taskProgressesList();
        when(this.taskAttemptGateway.findAllByUserId("1")).thenReturn(taskProgresses);

        // Act
        List<TaskProgress> foundTaskProgresses = this.getAllTaskProgress.execute("1");

        // Assert
        assertEquals(taskProgresses.size(), foundTaskProgresses.size());
    }

    @Test
    @DisplayName("Get All Task Progress - Task Progresses Do Not Exist")
    void execute_WhenTaskProgressesDoNotExist_ReturnsEmptyList() {
        // Arrange
        when(this.taskAttemptGateway.findAllByUserId("1")).thenReturn(List.of());

        // Act & Assert
        assertEquals(List.of(), this.getAllTaskProgress.execute("1"));
    }
}