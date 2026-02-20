package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.FirebaseTaskAttemptSchema;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseTaskAttemptRepository {
    private final Firestore firestore;

    public FirebaseTaskAttemptRepository(Firestore firestore) {
        this.firestore = firestore;
    }
    
    public Optional<FirebaseTaskAttemptSchema> findByUserIdAndTaskId(String userId, String taskId) {
        CollectionReference attempts = firestore.collection("task_attempts");
        Query query = attempts.whereEqualTo("userId", userId).whereEqualTo("taskId", taskId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                FirebaseTaskAttemptSchema schema = document.toObject(FirebaseTaskAttemptSchema.class);
                if (schema != null) {
                    schema.setId(document.getId());
                    return Optional.of(schema);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    public FirebaseTaskAttemptSchema save(FirebaseTaskAttemptSchema schema) {
        if (schema.getId() == null) {
            DocumentReference docRef = firestore.collection("task_attempts").document();
            schema.setId(docRef.getId());
        }
        firestore.collection("task_attempts").document(schema.getId()).set(schema);
        return schema;
    }

    public Integer countCompletedTasks(String userId, String lessonId) {
        CollectionReference attempts = firestore.collection("task_attempts");
        Query query = attempts.
                whereEqualTo("userId", userId).
                whereEqualTo("lessonId", lessonId).
                whereEqualTo("status", "COMPLETED");
        try {
            return query.get().get().size();
        } catch(InterruptedException | ExecutionException e) {
            return 0;
        }
    }

    public List<FirebaseTaskAttemptSchema> findAllByUserId(String userId) {
        CollectionReference attempts = firestore.collection("task_attempts");
        Query query = attempts.whereEqualTo("userId", userId);
        try {
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            return querySnapshot.get().getDocuments().stream()
                    .map(doc -> {
                        FirebaseTaskAttemptSchema schema = doc.toObject(FirebaseTaskAttemptSchema.class);
                        schema.setId(doc.getId());
                        return schema;
                    }).toList();
        } catch(InterruptedException | ExecutionException e) {
            return List.of();
        }
    }

    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("task_attempts").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (documents.isEmpty()) { return; }

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
            if (count > 0) { batch.commit().get(); }
        } catch (InterruptedException | ExecutionException ignored) {
        }
    }
}