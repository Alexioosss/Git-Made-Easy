package com.gitmadeeasy.infrastructure.gateways.users.repositories.firebase;

import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

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
                return Optional.ofNullable(document.toObject(UserSchema.class));
            } else {
                return Optional.empty();
            }
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
    public void deleteUser(String userId) {
        firestore.collection("users").document(userId).delete();
    }
}