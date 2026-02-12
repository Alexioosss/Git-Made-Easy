package com.gitmadeeasy.infrastructure.gateways.users;

public class UserSchema {

    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;


    // Empty constructor required for Firestore
    public UserSchema() {}

    public UserSchema(String firstName, String lastName, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
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

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() { return password; }

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