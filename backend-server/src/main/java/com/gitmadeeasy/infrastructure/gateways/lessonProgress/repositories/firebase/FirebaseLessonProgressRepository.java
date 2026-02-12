package com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.LessonProgressSchema;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.LessonProgressRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseLessonProgressRepository implements LessonProgressRepository {

    private final Firestore firestore;

    public FirebaseLessonProgressRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public LessonProgressSchema save(LessonProgressSchema lessonProgressSchema) {
        if (lessonProgressSchema.getId() == null) {
            DocumentReference docRef = firestore.collection("lesson_progress").document();
            lessonProgressSchema.setId(docRef.getId());
        }
        firestore.collection("lesson_progress").document(lessonProgressSchema.getId()).set(lessonProgressSchema);
        return lessonProgressSchema;
    }

    @Override
    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("lesson_progress").get();
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
