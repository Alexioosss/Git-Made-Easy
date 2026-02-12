package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseTaskAttemptRepository implements TaskAttemptRepository {

    private final Firestore firestore;

    public FirebaseTaskAttemptRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Optional<TaskAttemptSchema> findByUserIdAndTaskId(String userId, String taskId) {
        CollectionReference attempts = firestore.collection("task_attempts");
        Query query = attempts.whereEqualTo("userId", userId).whereEqualTo("taskId", taskId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                TaskAttemptSchema attempt = document.toObject(TaskAttemptSchema.class);
                if (attempt != null) {
                    attempt.setId(document.getId());
                    return Optional.of(attempt);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
        }
        return Optional.empty();
    }

    @Override
    public TaskAttemptSchema save(TaskAttemptSchema taskAttemptSchema) {
        if (taskAttemptSchema.getId() == null) {
            DocumentReference docRef = firestore.collection("task_attempts").document();
            taskAttemptSchema.setId(docRef.getId());
        }
        firestore.collection("task_attempts").document(taskAttemptSchema.getId()).set(taskAttemptSchema);
        return taskAttemptSchema;
    }

    @Override
    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("task_attempts").get();
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
