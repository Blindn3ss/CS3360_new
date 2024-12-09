package org.myapp.DAO;

import org.myapp.Database.Database;
import org.myapp.Model.Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class ManagerDAOImpl implements ManagerDAO {
    private static Connection connection;
    private static ManagerDAOImpl instance;

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

    @Override
    public boolean checkPermission(int managerId, int yardId) {
        String query = "SELECT EXISTS (SELECT 1 FROM permission WHERE managerId = ? AND yardId = ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, managerId);
            statement.setInt(2, yardId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addPermission(int managerId, int yardId){
        PreparedStatement preparedStatement;
        try {
            String query = "INSERT INTO permission (managerId, yardId) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            setQueryParameters(preparedStatement, managerId, yardId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: The yardId does not exist in the yard database.");
            System.out.println("Or 'this' manager already had the permission.");
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean removePermission(int managerId, int yardId) {
        PreparedStatement preparedStatement;
        try {
            String query = "DELETE FROM permission WHERE managerId = ? AND yardId = ?";
            preparedStatement = connection.prepareStatement(query);
            setQueryParameters(preparedStatement, managerId, yardId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Integer> getYardIdsForManager(int managerId) {
        List<Integer> yardIds = new ArrayList<>();
        String query = "SELECT yardId FROM permission WHERE managerId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, managerId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    yardIds.add(rs.getInt("yardId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yardIds;
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
}


