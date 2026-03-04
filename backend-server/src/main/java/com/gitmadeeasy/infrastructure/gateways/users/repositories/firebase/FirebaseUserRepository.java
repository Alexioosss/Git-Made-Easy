package com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.users.FirebaseUserSchema;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseUserRepository {
    private final Firestore firestore;

    public FirebaseUserRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public FirebaseUserSchema save(FirebaseUserSchema schema) {
        try {
            if (schema.getId() == null) {
                DocumentReference docRef = firestore.collection("users").document();
                schema.setId(docRef.getId());
            }
            ApiFuture<WriteResult> future = firestore.collection("users").document(schema.getId()).set(schema);
            future.get();
            return schema;
        } catch(InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public Optional<FirebaseUserSchema> findById(String userId) {
        DocumentReference docRef = firestore.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                FirebaseUserSchema schema = document.toObject(FirebaseUserSchema.class);
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

    public Optional<FirebaseUserSchema> findByEmailAddress(String emailAddress) {
        CollectionReference users = firestore.collection("users");
        Query query = users.whereEqualTo("emailAddress", emailAddress).limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                FirebaseUserSchema schema = document.toObject(FirebaseUserSchema.class);
                if(schema != null) {
                    schema.setId(document.getId());
                    return Optional.of(schema);
                }
            }
        } catch (InterruptedException | ExecutionException ignored) {}
        return Optional.empty();
    }

    public boolean existsByEmailAddress(String emailAddress) {
        CollectionReference users = firestore.collection("users");
        Query query = users.whereEqualTo("emailAddress", emailAddress).limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            return !querySnapshot.get().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("users").get();
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
        } catch (InterruptedException | ExecutionException ignored) {}
    }
}