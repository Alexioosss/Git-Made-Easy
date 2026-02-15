package com.gitmadeeasy.infrastructure.gateways.lessons.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.lessons.FirebaseLessonSchema;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseLessonRepository {
    private final Firestore firestore;

    public FirebaseLessonRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public FirebaseLessonSchema save(FirebaseLessonSchema schema) {
        if (schema.getId() == null) {
            DocumentReference docRef = firestore.collection("lessons").document();
            schema.setId(docRef.getId());
        }
        firestore.collection("lessons").document(schema.getId()).set(schema);
        return schema;
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