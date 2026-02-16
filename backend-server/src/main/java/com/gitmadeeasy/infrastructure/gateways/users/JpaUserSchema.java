package com.gitmadeeasy.infrastructure.gateways.users;

import jakarta.persistence.*;

@Entity @Table(name = "users")
public class JpaUserSchema {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;
    private String lastName;

    @Column(name = "email_address", unique = true)
    private String emailAddress;

    private boolean isEmailVerified;

    protected JpaUserSchema() {}

    public JpaUserSchema(String firstName, String lastName,
                         String emailAddress, boolean isEmailVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isEmailVerified = isEmailVerified;
    }

    public JpaUserSchema(String id, String firstName, String lastName,
                         String emailAddress,  boolean isEmailVerified) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isEmailVerified = isEmailVerified;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }
}