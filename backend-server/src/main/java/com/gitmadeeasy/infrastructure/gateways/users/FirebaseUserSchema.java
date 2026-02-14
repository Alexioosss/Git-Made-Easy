package com.gitmadeeasy.infrastructure.gateways.users;

public class FirebaseUserSchema {
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;

    protected FirebaseUserSchema() {}

    public FirebaseUserSchema(String firstName, String lastName, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
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

    public String getPassword() { return password; }

    public void setId(String id) {
        this.id = id;
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