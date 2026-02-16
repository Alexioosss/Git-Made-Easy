package com.gitmadeeasy.infrastructure.gateways.users;

public class FirebaseUserSchema {
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private boolean EmailVerified;

    protected FirebaseUserSchema() {}

    public FirebaseUserSchema(String firstName, String lastName, String emailAddress, boolean EmailVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.EmailVerified = EmailVerified;
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

    public boolean isEmailVerified() { return EmailVerified; }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmailVerified(boolean emailVerified) {
        EmailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "UserSchema{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}