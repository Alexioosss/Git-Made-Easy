package com.gitmadeeasy.infrastructure.gateways.lessons.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.lessons.FirebaseLessonSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.FirebaseTaskSchema;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseLessonRepository {
    private final Firestore firestore;

    public FirebaseLessonRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public FirebaseLessonSchema save(FirebaseLessonSchema schema) {
        try {
            if (schema.getId() == null) {
                DocumentReference docRef = firestore.collection("lessons").document();
                schema.setId(docRef.getId());
            }
            ApiFuture<WriteResult> future = firestore.collection("lessons").document(schema.getId()).set(schema);
            future.get();
            return schema;
        } catch(InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to save lesson", e);
        }
    }

    public Optional<FirebaseLessonSchema> findById(String lessonId) {
        DocumentReference docRef = firestore.collection("lessons").document(lessonId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                FirebaseLessonSchema schema = document.toObject(FirebaseLessonSchema.class);
                if (schema != null) {
                    schema.setId(document.getId());
                    return Optional.of(schema);
                }
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    public boolean existsById(String lessonId) {
        try {
            return firestore.collection("lessons").document(lessonId).get().get().exists();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    public List<FirebaseLessonSchema> findAll() {
        Query query = firestore.collection("lessons").orderBy("lessonOrder", Query.Direction.ASCENDING);
        try {
            QuerySnapshot snapshot = query.get().get();
            List<FirebaseLessonSchema> lessons = new ArrayList<>();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                FirebaseLessonSchema schema = doc.toObject(FirebaseLessonSchema.class);
                if (schema != null) {
                    schema.setId(doc.getId());
                    lessons.add(schema);
                }
            }
            return lessons;
        } catch (InterruptedException | ExecutionException e) {
            return List.of();
        }
    }

    public Integer findMaxLessonOrder() {
        CollectionReference lessons = firestore.collection("lessons");
        Query query = lessons.orderBy("lessonOrder", Query.Direction.DESCENDING).limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for(DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                FirebaseLessonSchema lesson = document.toObject(FirebaseLessonSchema.class);
                if(lesson != null) { return lesson.getLessonOrder(); }
            }
        } catch(InterruptedException | ExecutionException ignored) {}
        return 0;
    }

    public void updateTaskIds(String lessonId, List<String> taskIds) {
        try {
            ApiFuture<WriteResult> future = firestore.collection("lessons").document(lessonId).update("taskIds", taskIds);
            future.get();
        } catch(InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update taskIds for lesson " + lessonId, e);
        }
    }

    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("lessons").get();
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