package com.gitmadeeasy.testUtil;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.enums.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public final class TaskTestDataFactory {

    private TaskTestDataFactory() {}


    // ----- TASKS ----- //

    public static Task task() {
        return new Task(
                "1", "first git task", "Let's start this journey, shall we?",
                "git start", "easier than it may seem...", 1, DifficultyLevels.EASY);
    }

    public static JpaTaskSchema jpaTaskSchema() {
        return new JpaTaskSchema(
                "1", "first git task", "Let's start this journey, shall we?",
                "git start", "easier than it may seem...", 1, DifficultyLevels.EASY);
    }

    public static Task map(JpaTaskSchema schema) {
        return new Task(
                schema.getLessonId(), schema.getTitle(),
                schema.getContent(), schema.getExpectedCommand(),
                schema.getHint(), schema.getTaskOrder(), schema.getDifficulty());
    }

    public static Stream<Arguments> validTasksList() {
        return Stream.of(
                Arguments.of("Empty Tasks List", List.of()),
                Arguments.of("Populated Tasks List", List.of(jpaTaskSchema(), jpaTaskSchema()))
        );
    }


    // ----- TASK PROGRESS ----- //

    public static TaskProgress taskProgress() {
        TaskProgress progress = new TaskProgress("1", "1", "1");

        progress.setStatus(TaskCompletionStatus.IN_PROGRESS);
        progress.setAttempts(1);
        progress.setLastInput("input");
        progress.setLastError(null);
        progress.setStartedAt(LocalDate.now().minusDays(1));
        progress.setCompletedAt(null);

        return progress;
    }

    public static TaskProgress completedTaskProgress() {
        TaskProgress progress = new TaskProgress("1", "1", "1");

        progress.setStatus(TaskCompletionStatus.COMPLETED);
        progress.setAttempts(1);
        progress.setLastInput("input");
        progress.setLastError(null);
        progress.setStartedAt(LocalDate.now().minusDays(1));
        progress.setCompletedAt(LocalDate.now());
        return progress;
    }

    public static TaskProgress taskProgressWithId(String id) {
        TaskProgress progress = taskProgress();
        return new TaskProgress(
                id, progress.getUserId(), progress.getTaskId(),
                progress.getLessonId(), progress.getTaskTitle(),
                progress.getStatus(), progress.getAttempts(),
                progress.getLastInput(), progress.getLastError(),
                progress.getStartedAt(), progress.getCompletedAt()
        );
    }


    // ----- TASK ATTEMPT SCHEMAS ----- //

    public static JpaTaskAttemptSchema taskAttemptSchema() {
        return new JpaTaskAttemptSchema(
                "1", TaskCompletionStatus.IN_PROGRESS, 1,
                "input", "", LocalDate.now(), null,
                TaskTestDataFactory.jpaTaskSchema()
        );
    }

    public static JpaTaskAttemptSchema taskAttemptSchemaWithId(String id) {
        JpaTaskAttemptSchema schema = taskAttemptSchema();
        schema.setId(id);
        return schema;
    }
}