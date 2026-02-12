package com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseUserRepository implements UserRepository {
    private final Firestore firestore;

    public FirebaseUserRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public UserSchema save(UserSchema userSchema) {
        if (userSchema.getId() == null) {
            DocumentReference docRef = firestore.collection("users").document();
            userSchema.setId(docRef.getId());
        }
        firestore.collection("users").document(userSchema.getId()).set(userSchema);
        return userSchema;
    }

    @Override
    public Optional<UserSchema> findById(String userId) {
        DocumentReference docRef = firestore.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                UserSchema user = document.toObject(UserSchema.class);
                if (user != null) {
                    user.setId(document.getId());
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserSchema> findByEmail(String emailAddress) {
        CollectionReference users = firestore.collection("users");
        Query query = users.whereEqualTo("emailAddress", emailAddress).limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                UserSchema user = document.toObject(UserSchema.class);
                if(user != null) {
                    user.setId(document.getId());
                    return Optional.of(user);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String emailAddress) {
        CollectionReference users = firestore.collection("users");
        Query query = users.whereEqualTo("emailAddress", emailAddress).limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            return !querySnapshot.get().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            // Handle exception
            return false;
        }
    }

    @Override
    public void deleteAll() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("users").get();
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
