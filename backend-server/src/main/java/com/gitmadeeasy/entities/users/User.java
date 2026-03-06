package com.gitmadeeasy.entities.users;

import java.util.Objects;

public class User {
    public String firebaseUid;
    private String userId;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public User(String userId, String firstName, String lastName, String emailAddress) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public String getFirebaseUid() { return firebaseUid; }

    public String getUserId() {
        return userId;
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

    public void setUserId(String id) {
        this.userId = id;
    }

    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }

    @Override
    public String toString() {
        return "User{" +
                "firebaseUid='" + firebaseUid + '\'' +
                "id='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(emailAddress, user.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, emailAddress);
    }
}