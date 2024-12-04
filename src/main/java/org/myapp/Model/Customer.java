package org.myapp.Model;

public class Customer {
    private int customerId;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;

    public Customer(int customerId, String username, String password,
                    String fullName, String phoneNumber, String email) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
        System.out.println("\nCustomer Profile Details:");
        System.out.println("Customer ID: " + customerId);
        System.out.println("Username: " + username);
        System.out.println("Full Name: " + fullName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
    }

    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", username=" + username +
                ", fullName=" + fullName + ", phoneNumber=" + phoneNumber +
                ", email=" + email + "]";
    }

}

