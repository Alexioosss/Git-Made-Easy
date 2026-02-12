package com.gitmadeeasy.infrastructure.gateways.lessons.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseLessonRepository implements LessonRepository {

    private final Firestore firestore;

    public FirebaseLessonRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public LessonSchema save(LessonSchema lessonSchema) {
        if (lessonSchema.getId() == null) {
            DocumentReference docRef = firestore.collection("lessons").document();
            lessonSchema.setId(docRef.getId());
        }
        firestore.collection("lessons").document(lessonSchema.getId()).set(lessonSchema);
        return lessonSchema;
    }

    @Override
    public Optional<LessonSchema> findById(String lessonId) {
        DocumentReference docRef = firestore.collection("lessons").document(lessonId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                LessonSchema lesson = document.toObject(LessonSchema.class);
                if (lesson != null) {
                    lesson.setId(document.getId());
                    return Optional.of(lesson);
                }
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(String lessonId) {
        DocumentReference docRef = firestore.collection("lessons").document(lessonId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            return future.get().exists();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    @Override
    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("lessons").get();
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
