package org.myapp.DAO;

import org.myapp.Database.Database;
import org.myapp.Model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class CustomerDAOImpl implements CustomerDAO {
    private static Connection connection;
    private static CustomerDAOImpl instance;

    public CustomerDAOImpl() {
        Database db = new Database();
        connection = db.connect();
    }

    public static CustomerDAOImpl getInstance() {
        if (instance == null) {
            instance = new CustomerDAOImpl();
        }
        return instance;
    }

    @Override
    public boolean createCustomer(Customer customer) {
        String query = "INSERT INTO customer (username, password, fullName, phoneNumber, email) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(   query,
                                customer.getUsername(),
                                customer.getPassword(),
                                customer.getFullName(),
                                customer.getPhoneNumber(),
                                customer.getEmail());
    }

    @Override
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customer WHERE customerId = ?";
        return executeQuery(query, customerId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customer SET username = ?, password = ?, fullName = ?, phoneNumber = ?, email = ? WHERE customerId = ?";
        return executeUpdate(   query,
                                customer.getUsername(),
                                customer.getPassword(),
                                customer.getFullName(),
                                customer.getPhoneNumber(),
                                customer.getEmail(),
                                customer.getCustomerId());
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        String query = "DELETE FROM customer WHERE customerId = ?";
        return executeUpdate(query, customerId);
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        String query = "SELECT * FROM customer WHERE username = ?";
        List<Customer> customers = executeQuery(query, username);
        return customers.isEmpty() ? null : customers.getFirst();
    }

    @Override
    public List<Customer> getAllCustomers() {
        String query = "SELECT * FROM customer";
        return executeQuery(query);
    }

    // Generalized method for executing update queries (insert, update, delete)
    private boolean executeUpdate(String query, Object... params) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, params);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private List<Customer> executeQuery(String query, Object... params) {
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(customerRowMapper(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // Method for setting query parameters (generic for any select query)
    // We can decide the order sent to this func.
    private void setQueryParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    private Customer customerRowMapper(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getInt("customerId"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("fullName"),
                resultSet.getString("phoneNumber"),
                resultSet.getString("email")
        );
    }
}

