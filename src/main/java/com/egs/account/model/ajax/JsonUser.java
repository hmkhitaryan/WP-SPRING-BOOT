package com.egs.account.model.ajax;

import java.util.Objects;

public class JsonUser {
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JsonUser() {
    }

    public JsonUser(String username, String firstName, String lastName, String email, String skypeID) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonUser jsonUser = (JsonUser) o;
        return Objects.equals(username, jsonUser.username) &&
                Objects.equals(firstName, jsonUser.firstName) &&
                Objects.equals(lastName, jsonUser.lastName) &&
                Objects.equals(email, jsonUser.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, firstName, lastName, email);
    }
}
