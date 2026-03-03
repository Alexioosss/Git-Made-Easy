package com.gitmadeeasy.infrastructure.configs.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component @Profile("!test")
public class LessonsDataLoader implements CommandLineRunner {
    private final LessonGateway lessonGateway;
    private final TaskGateway taskGateway;
    private final SeedProperties seedProperties;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(LessonsDataLoader.class);

    public LessonsDataLoader(LessonGateway lessonGateway, TaskGateway taskGateway, SeedProperties seedProperties,
                             ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
        this.seedProperties = seedProperties;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        if(!this.lessonGateway.findAllLessons().isEmpty()) {
            log.info("Startup lessons seeding skipped. Lessons already exist.");
            return;
        }

        log.info("Seeding default lessons...");
        List<Lesson> lessons = new ArrayList<>();

        if(this.seedProperties.isFromFile()) {
            log.info("Loading lessons data from file.");
            for(String filePath : this.seedProperties.getFilePath()) {
                try {
                    List<Lesson> loaded = loadFromFile(filePath);
                    if(loaded.isEmpty()){ log.warn("Lesson file {} was empty or missing.", filePath); }
                    else {
                        log.info("Loaded {} lessons from file: {}", loaded.size(), filePath);
                        lessons.addAll(loaded);
                    }
                } catch(Exception e) {
                    log.error("Failed to load lessons from file {}. Skipping.", filePath, e);
                }
            }
            if(lessons.isEmpty()) {
                log.warn("No lessons were loaded from any file. Falling back to default lessons.");
                lessons = LessonSeedData.getLessons();
            }
        } else {
            lessons = LessonSeedData.getLessons();
            log.info("Loaded default lessons.");
        }

        seedLessons(lessons);
        log.info("Lessons seeding completed successfully.");
    }

    private List<Lesson> loadFromFile(String filePath) {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            if(!resource.exists()) { log.warn("Resource not found at path: {}", filePath); return List.of(); }
            try (InputStream inputStream = resource.getInputStream()) {
                return this.objectMapper.readValue(inputStream, new TypeReference<>() {});
            }
        } catch(Exception e) {
            throw new RuntimeException("Failed to load lessons from file: " + filePath, e);
        }
    }

    private void seedLessons(List<Lesson> lessons) {
        for(Lesson lesson : lessons) {
            Lesson savedLesson = this.lessonGateway.createLesson(lesson);
            List<String> taskIds = new ArrayList<>();
            for(Task task : lesson.getTasks()) {
                task.setTaskId(null);
                task.setLessonId(savedLesson.getLessonId());
                Task savedTask = this.taskGateway.createTask(task);
                taskIds.add(savedTask.getTaskId());
            }
            this.lessonGateway.updateTaskIds(savedLesson.getLessonId(), taskIds);
        }
    }
}