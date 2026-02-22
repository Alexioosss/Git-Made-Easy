package com.gitmadeeasy.infrastructure.gateways.tasks.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.tasks.FirebaseTaskSchema;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseTaskRepository {
    private final Firestore firestore;

    public FirebaseTaskRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public FirebaseTaskSchema save(FirebaseTaskSchema schema) {
        if(schema.getId() == null) {
            DocumentReference docRef = firestore.collection("tasks").document();
            schema.setId(docRef.getId());
        }
        firestore.collection("tasks").document(schema.getId()).set(schema);
        return schema;
    }

    public Optional<FirebaseTaskSchema> findByLessonIdAndTaskId(String lessonId, String taskId) {
        DocumentReference docRef = firestore.collection("tasks").document(taskId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if(document.exists()) {
                FirebaseTaskSchema task = document.toObject(FirebaseTaskSchema.class);
                if (task != null && lessonId.equals(task.getLessonId())) {
                    task.setId(document.getId());
                    return Optional.of(task);
                }
            }
            return Optional.empty();
        } catch(InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    public List<FirebaseTaskSchema> findAllByLessonId(String lessonId) {
        CollectionReference tasks = firestore.collection("tasks");
        Query query = tasks.whereEqualTo("lessonId", lessonId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<FirebaseTaskSchema> result = new ArrayList<>();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                FirebaseTaskSchema task = document.toObject(FirebaseTaskSchema.class);
                if(task != null) {
                    task.setId(document.getId());
                    result.add(task);
                }
            }
        } catch(InterruptedException | ExecutionException ignored) {
        }
        return result;
    }

    public Optional<FirebaseTaskSchema> findById(String taskId) {
        DocumentReference docRef = firestore.collection("tasks").document(taskId);
        try {
            DocumentSnapshot doc = docRef.get().get();
            if(doc.exists()) {
                FirebaseTaskSchema schema = doc.toObject(FirebaseTaskSchema.class);
                if(schema != null) {
                    schema.setId(doc.getId());
                    return Optional.of(schema);
                }
            }
        } catch(Exception ignored) {}
        return Optional.empty();
    }

    public boolean existsById(String taskId) {
        DocumentReference docRef = firestore.collection("tasks").document(taskId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            return future.get().exists();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    public Integer findMaxTaskOrderByLessonId(String lessonId) {
        CollectionReference tasks = firestore.collection("tasks");
        Query query = tasks.whereEqualTo("lessonId", lessonId)
                .orderBy("taskOrder", Query.Direction.DESCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for(DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                FirebaseTaskSchema task = document.toObject(FirebaseTaskSchema.class);
                if(task != null) { return task.getTaskOrder(); }
            }
        } catch (InterruptedException | ExecutionException ignored) {}
        return 0;
    }

    public Integer countTasksByLessonId(String lessonId) {
        try {
            Query query = firestore.collection("tasks").whereEqualTo("lessonId", lessonId);
            return query.get().get().size();
        } catch(InterruptedException | ExecutionException e) {
            return 0;
        }
    }

    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("tasks").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if(documents.isEmpty()) { return; }

            WriteBatch batch = firestore.batch();
            int count = 0;
            for (QueryDocumentSnapshot document : documents) {
                batch.delete(document.getReference());
                count++;
                if(count == 500) {
                    batch.commit().get();
                    batch = firestore.batch();
                    count = 0;
                }
            }
            if(count > 0) { batch.commit().get(); }
        } catch (InterruptedException | ExecutionException ignored) {
        }
    }
}