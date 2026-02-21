package com.gitmadeeasy.unit.usecases.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.DifficultyLevelNotRecognisedException;
import com.gitmadeeasy.usecases.validation.exceptions.MissingRequiredFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLessonTest {
    @Mock private LessonGateway lessonGateway;
    @InjectMocks private CreateLesson createLesson;


    @Test
    @DisplayName("Create A Lesson - Valid Payload")
    void execute_WhenValidPayload_ReturnsCreatedLesson() {
        // Arrange
        CreateLessonRequest request = new CreateLessonRequest(
                "Intro to Git",
                "A simple overview of an industry-famous version control system.",
                "easy", 1);
        Lesson createdLesson = new Lesson(
                "1", "Intro to Git",
                "A simple overview of an industry-famous version control system.",
                DifficultyLevels.EASY, 1);
        when(this.lessonGateway.createLesson(any(Lesson.class))).thenReturn(createdLesson);

        // Act
        Lesson result = this.createLesson.execute(request);

        // Assert
        assertEquals("1", result.getLessonId());
        assertEquals("Intro to Git", result.getTitle());
        assertEquals("A simple overview of an industry-famous version control system.", result.getDescription());
        assertEquals(DifficultyLevels.EASY, result.getDifficulty());
    }

    @ParameterizedTest
    @MethodSource("invalidFieldsProvider")
    @DisplayName("Create A Lesson - Invalid Payload")
    void execute_WhenInvalidPayload_ThrowsMissingRequiredFieldException(
            String title, String description, String difficulty, Integer lessonOrder, String expectedErrorMessage) {
        // Arrange
        CreateLessonRequest request = new CreateLessonRequest(title, description, difficulty, lessonOrder);

        // Act
        MissingRequiredFieldException ex = assertThrows(MissingRequiredFieldException.class, () -> this.createLesson.execute(request));

        // Assert
        assertEquals(expectedErrorMessage, ex.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "veryhard",
            "undefined",
            "easyish",
            "easy123"
    })
    @DisplayName("Create A Lesson - Lesson Difficulty Is Invalid/Not Recognised")
    void execute_WhenInvalidDifficulty_ThrowsLessonDifficultyNotRecognisedException(String invalidDifficulty) {
        // Arrange
        CreateLessonRequest request = new CreateLessonRequest("Title", "Description", invalidDifficulty, 1);

        // Act
        DifficultyLevelNotRecognisedException ex = assertThrows(DifficultyLevelNotRecognisedException.class,
                () -> this.createLesson.execute(request));

        // Assert
        assertTrue(ex.getMessage().contains(invalidDifficulty));
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static Stream<Arguments> invalidFieldsProvider() {
        return Stream.of(
                Arguments.of(null, "Description", "easy", 1, "title cannot be left blank"),
                Arguments.of("", "Description", "medium", 1, "title cannot be left blank"),
                Arguments.of(" ", "Description", "hard", 1, "title cannot be left blank"),

                Arguments.of("Title", null, "hard", 1, "task description cannot be left blank"),
                Arguments.of("Title", "", "medium", 1, "task description cannot be left blank"),
                Arguments.of("Title", " ", "easy", 1, "task description cannot be left blank"),

                Arguments.of("Title", "Description", null, 1, "task difficulty cannot be left blank"),
                Arguments.of("Title", "Description", "", 1, "task difficulty cannot be left blank"),
                Arguments.of("Title", "Description", " ", 1, "task difficulty cannot be left blank")
        );
    }
}