package org.myapp.DAO;

import org.myapp.Model.Customer;

import java.util.List;

public interface CustomerDAO {

    boolean createCustomer(Customer customer);

    Customer getCustomerById(int customerId);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(int customerId);

    Customer getCustomerByUsername(String username);

    List<Customer> getAllCustomers();

}


