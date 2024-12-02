package org.myapp.DAO;

import org.myapp.Database.Database;
import org.myapp.Model.Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class ManagerDAOImpl implements ManagerDAO {
    private final Connection connection;
    private static ManagerDAOImpl instance;

    // Constructor to initialize database connection
    public ManagerDAOImpl() {
        Database db = new Database();
        connection = db.connect();
    }

    public static ManagerDAOImpl getInstance(){
        if (instance == null) {
            instance = new ManagerDAOImpl();
        }
        return instance;
    }

    @Override
    public boolean createManager(Manager manager) {
        String query = "INSERT INTO manager (username, password, fullName, phoneNumber, email) VALUES ( ?, ?, ?, ?, ?)";
        return executeUpdate(query, manager.getUsername(),
                                    manager.getPassword(),
                                    manager.getFullName(),
                                    manager.getPhoneNumber(),
                                    manager.getEmail());
    }

    @Override
    public Manager getManagerById(int managerId) {
        String query = "SELECT * FROM manager WHERE managerId = ?";
        return executeQuery(query, managerId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateManager(Manager manager) {
        String query = "UPDATE manager SET username = ?, password = ?, fullName = ?, phoneNumber = ?, email = ? WHERE managerId = ?";
        return executeUpdate(query, manager.getUsername(),
                                    manager.getPassword(),
                                    manager.getFullName(),
                                    manager.getPhoneNumber(),
                                    manager.getEmail(),
                                    manager.getManagerId());
    }

    @Override
    public boolean deleteManager(int managerId) {
        String query = "DELETE FROM manager WHERE managerId = ?";
        return executeUpdate(query, managerId);
    }

    @Override
    public Manager getManagerByUserName(String username) {
        String query = "SELECT * FROM manager WHERE username = ?";
        List<Manager> managers = executeQuery(query, username);
        return managers.isEmpty() ? null : managers.getFirst();  // Return the first match, or null if not found
    }

    @Override
    public List<Manager> getAllManagers() {
        String query = "SELECT * FROM manager";
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

    // Generalized method for executing select queries and mapping the result set
    private List<Manager> executeQuery(String query, Object... params) {
        List<Manager> managers = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    managers.add(managerRowMapper(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return managers;
    }

    // Method for setting query parameters (generic for any select query)
    private void setQueryParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    // Method to map a ResultSet row to a Manager object
    private Manager managerRowMapper(ResultSet resultSet) throws SQLException {
        return new Manager(
                resultSet.getInt("managerId"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("fullName"),
                resultSet.getString("phoneNumber"),
                resultSet.getString("email")
        );
    }

    // Close the database connection when done
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


