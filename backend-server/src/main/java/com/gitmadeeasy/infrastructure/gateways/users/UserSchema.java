package com.gitmadeeasy.infrastructure.gateways.users;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class UserSchema {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;


    // Empty constructor required by JPA
    protected UserSchema() {}

    public UserSchema(String firstName, String lastName, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserSchema that = (UserSchema) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}