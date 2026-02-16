package com.gitmadeeasy.entities.users;

import java.util.Objects;

public class User {
    private String userId;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private boolean isEmailVerified;

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isEmailVerified = false;
    }

    public User(String userId, String firstName, String lastName, String emailAddress, boolean isEmailVerified) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isEmailVerified = isEmailVerified;
    }

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

    public boolean isEmailVerified() {
        return this.isEmailVerified;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public void setEmailVerified(boolean emailVerified) { isEmailVerified = emailVerified; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", isEmailVerified=" + isEmailVerified +
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