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
        if(!this.lessonGateway.findAllLessons().isEmpty()) { // If there are lessons in the database, skip the default seeding
            log.info("Startup lessons seeding skipped. Lessons already exist.");
            return;
        }

        log.info("Seeding default lessons...");
        List<Lesson> lessons = new ArrayList<>();

        if(this.seedProperties.isFromFile()) { // If the setting to seed from files is enabled
            log.info("Loading lessons data from file.");
            for(String filePath : this.seedProperties.getFilePath()) { // Read each file path and read the seed data from the files
                try {
                    List<Lesson> loaded = loadFromFile(filePath);
                    if(loaded.isEmpty()){ log.warn("Lesson file {} was empty or missing.", filePath); } // If file is empty, log and skip to next file
                    else {
                        log.info("Loaded {} lessons from file: {}", loaded.size(), filePath);
                        lessons.addAll(loaded);
                    }
                } catch(Exception e) {
                    log.error("Failed to load lessons from file {}. Skipping.", filePath, e);
                }
            }
            if(lessons.isEmpty()) { // If the file(s) had no data, skip seeding from files
                log.warn("No lessons were loaded from any file. Falling back to default lessons.");
                lessons = LessonSeedData.getLessons(); // Seed the lessons from the Java file instead; infrastructure.configs.seed.LessonSeedData
            }
        } else { // If the setting to seed from files is disabled
            lessons = LessonSeedData.getLessons(); // Seed from the Java file instead
            log.info("Loaded default lessons.");
        }

        seedLessons(lessons); // Save them in the data store through the repository directly
        log.info("Lessons seeding completed successfully.");
    }

    /**
     * Method to load data from a file path and parse it to the required object for lessons seeding
     * Method can throw an exception if an issue occurs during file reading or object parsing
     * @param filePath The filepath string that will point to the file to be read and parsed into the required object
     * @return returns a List of parsed Lesson objects after reading them from the file
     */
    private List<Lesson> loadFromFile(String filePath) {
        try {
            Resource resource = this.resourceLoader.getResource(filePath);
            if(!resource.exists()) { log.warn("Resource not found at path: {}", filePath); return List.of(); }
            try (InputStream inputStream = resource.getInputStream()) {
                return this.objectMapper.readValue(inputStream, new TypeReference<>() {});
            }
        } catch(Exception e) {
            throw new RuntimeException("Failed to load lessons from file: " + filePath, e);
        }
    }

    /**
     * Method to persist the lessons that have been generated and / or parsed, either via external file, or via internal Java class
     * @param lessons The list of lessons to be seeded, or saved, into the data store
     */
    private void seedLessons(List<Lesson> lessons) {
        for(Lesson lesson : lessons) { // For each lesson in the list of lessons to save
            Lesson savedLesson = this.lessonGateway.createLesson(lesson); // Save the current lesson
            List<String> taskIds = new ArrayList<>(); // Keep track of the current lesson's task IDs to also save
            for(Task task : lesson.getTasks()) { // For each tasks in the lesson's list of tasks
                task.setTaskId(null); // Make the data store generate the taskID
                task.setLessonId(savedLesson.getLessonId()); // set the lessonID from the saved lesson object
                Task savedTask = this.taskGateway.createTask(task); // Save the task in the data store
                taskIds.add(savedTask.getTaskId()); // Add the saved task's taskID to the list of tasks for the current lesson
            }
            // After all tasks are saved for the lesson, and all auto-generated taskIDs have been gathered
            // Update the lesson's list of taskIDs with the latest, up-to-date version of the taskIDs
            this.lessonGateway.updateTaskIds(savedLesson.getLessonId(), taskIds);
        }
    }
}