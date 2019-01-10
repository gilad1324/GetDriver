package com.jct.gilad.getdriver.model.entities;

public class Driver {
    private String lastName;
    private String firstName;
    private String password;
    private String id;
    private String phoneNumber;
    private String email;
    private String creditCard;

    public Driver(String lastName, String firstName, String password, String id, String phoneNumber, String email, String creditCard) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.creditCard = creditCard;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
