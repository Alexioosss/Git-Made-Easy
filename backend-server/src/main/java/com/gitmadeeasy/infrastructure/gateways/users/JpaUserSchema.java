package com.gitmadeeasy.infrastructure.gateways.users;

import jakarta.persistence.*;

@Entity @Table(name = "users")
public class JpaUserSchema {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firebaseUid;
    private String firstName;
    private String lastName;

    @Column(name = "email_address", unique = true)
    private String emailAddress;

    protected JpaUserSchema() {}

    public JpaUserSchema(String firebaseUid, String firstName, String lastName,
                         String emailAddress) {
        this.firebaseUid = firebaseUid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public String getId() {
        return id;
    }

    public String getFirebaseUid() { return firebaseUid; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}