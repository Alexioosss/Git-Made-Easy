package com.gitmadeeasy.infrastructure.gateways.tasks.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseTaskRepository implements TaskRepository {

    private final Firestore firestore;

    public FirebaseTaskRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public TaskSchema save(TaskSchema taskSchema) {
        if (taskSchema.getId() == null) {
            DocumentReference docRef = firestore.collection("tasks").document();
            taskSchema.setId(docRef.getId());
        }
        firestore.collection("tasks").document(taskSchema.getId()).set(taskSchema);
        return taskSchema;
    }

    @Override
    public Optional<TaskSchema> findByLessonIdAndTaskId(String lessonId, String taskId) {
        DocumentReference docRef = firestore.collection("tasks").document(taskId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                TaskSchema task = document.toObject(TaskSchema.class);
                if (task != null && lessonId.equals(task.getLessonId())) {
                    task.setId(document.getId());
                    return Optional.of(task);
                }
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
            return Optional.empty();
        }
    }

    @Override
    public List<TaskSchema> findAllByLessonId(String lessonId) {
        CollectionReference tasks = firestore.collection("tasks");
        Query query = tasks.whereEqualTo("lessonId", lessonId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<TaskSchema> result = new ArrayList<>();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                TaskSchema task = document.toObject(TaskSchema.class);
                if (task != null) {
                    task.setId(document.getId());
                    result.add(task);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
        }
        return result;
    }

    @Override
    public boolean existsById(String taskId) {
        DocumentReference docRef = firestore.collection("tasks").document(taskId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            return future.get().exists();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    @Override
    public Integer findMaxTaskOrderByLessonId(String lessonId) {
        CollectionReference tasks = firestore.collection("tasks");
        Query query = tasks.whereEqualTo("lessonId", lessonId)
                .orderBy("taskOrder", Query.Direction.DESCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                TaskSchema task = document.toObject(TaskSchema.class);
                if (task != null) {
                    return task.getTaskOrder();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
        }
        return 0;
    }

    @Override
    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("tasks").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (documents.isEmpty()) {
                return;
            }

            WriteBatch batch = firestore.batch();
            int count = 0;
            for (QueryDocumentSnapshot document : documents) {
                batch.delete(document.getReference());
                count++;
                if (count == 500) {
                    batch.commit().get();
                    batch = firestore.batch();
                    count = 0;
                }
            }
            if (count > 0) {
                batch.commit().get();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
        }
    }
}
