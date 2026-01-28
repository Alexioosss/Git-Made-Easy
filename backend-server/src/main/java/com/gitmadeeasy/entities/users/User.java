package com.gitmadeeasy.entities.users;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private boolean isEmailVerified;

    public User(String id, String firstName, String lastName, String emailAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isEmailVerified = false;
    }

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isEmailVerified = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isEmailVerified() {
        return this.isEmailVerified;
    }

    public void markEmailAsVerified() {
        if(this.isEmailVerified) {
            throw new IllegalArgumentException("The Email Address Has Already Been Verified.");
        }
        this.isEmailVerified = true;
    }
}