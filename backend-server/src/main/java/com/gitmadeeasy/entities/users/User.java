package com.gitmadeeasy.entities.users;

import com.gitmadeeasy.usecases.users.exceptions.EmailAlreadyVerifiedException;

import java.util.Objects;

public class User {
    private String userId;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String password;
    private boolean isEmailVerified;

    public User(String userId, String firstName, String lastName, String emailAddress, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.isEmailVerified = false;
    }

    public User(String firstName, String lastName, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.isEmailVerified = false;
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

    public String getPassword() {
        return password;
    }

    public boolean isEmailVerified() {
        return this.isEmailVerified;
    }

    public void markEmailAsVerified() {
        if(this.isEmailVerified) { throw new EmailAlreadyVerifiedException(); }
        this.isEmailVerified = true;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(emailAddress, user.emailAddress) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, emailAddress, password);
    }

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
}