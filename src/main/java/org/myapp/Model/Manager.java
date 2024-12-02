package org.myapp.Model;

import java.util.List;

public class Manager {
    private int managerId;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;

    public Manager(int managerId, String username, String password, String fullName, String phoneNumber, String email) {
        this.managerId = managerId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public void viewProfileDetails() {
        System.out.println("\nManager Profile Details:");
        System.out.println("Manager ID: " + managerId);
        System.out.println("Username: " + username);
        System.out.println("Full Name: " + fullName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
    }

    @Override
    public String toString() {
        return "Manager [managerId=" + managerId + ", username=" + username +
                ", fullName=" + fullName + ", phoneNumber=" + phoneNumber +
                ", email=" + email + "]";
    }
}

