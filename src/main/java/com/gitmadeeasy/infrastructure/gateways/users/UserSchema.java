package com.gitmadeeasy.infrastructure.gateways.users;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class UserSchema {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;

    public UserSchema(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public Long getId() {
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

    @Override
    public String toString() {
        return String.format("%s %s, Email Address: %s", firstName, lastName, emailAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserSchema that = (UserSchema) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, emailAddress);
    }
}