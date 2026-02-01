package com.gitmadeeasy.entities.users;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private boolean isEmailVerified;

    public User(String id, String firstName, String lastName, String emailAddress, String password) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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