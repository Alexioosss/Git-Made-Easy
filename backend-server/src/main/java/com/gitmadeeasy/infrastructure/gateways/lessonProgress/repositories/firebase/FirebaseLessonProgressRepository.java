package com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.FirebaseLessonProgressSchema;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseLessonProgressRepository {
    private final Firestore firestore;

    public FirebaseLessonProgressRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public FirebaseLessonProgressSchema save(FirebaseLessonProgressSchema schema) {
        if (schema.getId() == null) {
            DocumentReference docRef = firestore.collection("lesson_progress").document();
            schema.setId(docRef.getId());
        }
        firestore.collection("lesson_progress").document(schema.getId()).set(schema);
        return schema;
    }

    public Optional<FirebaseLessonProgressSchema> findByUserIdAndLessonId(String userId, String lessonId) {
        CollectionReference collection = firestore.collection("lesson_progress");
        Query query = collection.whereEqualTo("userId", userId).whereEqualTo("lessonId", lessonId).limit(1);

        try {
            QuerySnapshot snapshot = query.get().get();
            for(DocumentSnapshot document : snapshot.getDocuments()) {
                FirebaseLessonProgressSchema schema = document.toObject(FirebaseLessonProgressSchema.class);
                if(schema != null) {
                    schema.setId(document.getId());
                    return Optional.of(schema);
                }
            }
            return Optional.empty();
        } catch(InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    public List<FirebaseLessonProgressSchema> findAllByUserId(String userId) {
        CollectionReference progress = firestore.collection("lesson_progress");
        Query query = progress.whereEqualTo("userId", userId);
        try {
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            return querySnapshot.get().getDocuments().stream()
                    .map(doc -> {
                        FirebaseLessonProgressSchema schema = doc.toObject(FirebaseLessonProgressSchema.class);
                        schema.setId(doc.getId());
                        return schema;
                    }).toList();
        } catch(InterruptedException | ExecutionException e) {
            return List.of();
        }
    }

    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("lesson_progress").get();
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