package org.myapp.DAO;

import org.myapp.Model.Customer;

import java.util.List;

public interface CustomerDAO {

    // Create a new customer in the database
    boolean createCustomer(Customer customer);

    // Get a customer by their unique ID
    Customer getCustomerById(int customerId);

    // Update an existing customer's details
    boolean updateCustomer(Customer customer);

    // Delete a customer by their unique ID
    boolean deleteCustomer(int customerId);

    // Get a customer by their username
    Customer getCustomerByUsername(String username);

    // Get all customers from the database
    List<Customer> getAllCustomers();

    // Get a customer by full name and phone number
    Customer getCustomerByFullNameAndPhoneNumber(String fullName, String phoneNumber);

    // General method for executing update queries (insert, update, delete)
    boolean executeUpdate(String query, Object... params);

    // General method for executing select queries (retrieves customers)
    List<Customer> executeQuery(String query, Object... params);
}


