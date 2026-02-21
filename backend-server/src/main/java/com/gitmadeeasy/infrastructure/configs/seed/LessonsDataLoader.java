package com.gitmadeeasy.infrastructure.configs.seed;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component @Profile("!test")
public class LessonsDataLoader implements CommandLineRunner {
    private final LessonGateway lessonGateway;
    private final TaskGateway taskGateway;

    public LessonsDataLoader(LessonGateway lessonGateway, TaskGateway taskGateway) {
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
    }

    @Override
    public void run(String... args) {
        if(lessonGateway.findAllLessons().isEmpty()) {
            System.out.println("Seeding default lessons...");

            LessonSeedData.getLessons().forEach(lesson -> {
                Lesson savedLesson = this.lessonGateway.createLesson(lesson);
                List<String> taskIds = new ArrayList<>();

                lesson.getTasks().forEach(task -> {
                    task.setTaskId(null);
                    task.setLessonId(savedLesson.getLessonId());

                    Task savedTask = this.taskGateway.createTask(task);
                    taskIds.add(savedTask.getTaskId());
                });
                this.lessonGateway.updateTaskIds(savedLesson.getLessonId(), taskIds);
            });

            System.out.println("Seeded default lessons successfully.");
        } else { System.out.println("Startup lessons seeding skipped. Lessons already exist."); }
    }
}