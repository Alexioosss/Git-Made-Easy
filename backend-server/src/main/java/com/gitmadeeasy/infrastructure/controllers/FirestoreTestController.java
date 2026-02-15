package com.gitmadeeasy.infrastructure.controllers;

import com.google.cloud.firestore.Firestore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirestoreTestController {
    private final Firestore firestore;

    public FirestoreTestController(Firestore firestore) {
        this.firestore = firestore;
    }

    @GetMapping("/firestore-test")
    public String test() {
        return firestore != null ? "Firestore is connected" : "Firestore is NOT connected";
    }
}