package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component @Profile("!test")
public class LessonsDataLoader implements CommandLineRunner {
    private final LessonGateway lessonGateway;

    public LessonsDataLoader(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    @Override
    public void run(String... args) {
        if(lessonGateway.findAllLessons().isEmpty()) {
            System.out.println("Seeding default lessons...");

            this.lessonGateway.createLesson(new Lesson(
                    "Intro to Git", "Basics of Git", DifficultyLevels.EASY, 1));
            this.lessonGateway.createLesson(new Lesson(
                    "Branching", "Learn branching in Git", DifficultyLevels.MEDIUM, 2));
            System.out.println("Seeded default lessons successfully.");
        }
    }
}