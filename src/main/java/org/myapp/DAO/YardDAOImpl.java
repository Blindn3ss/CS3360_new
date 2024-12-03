package org.myapp.DAO;

import org.myapp.Database.Database;
import org.myapp.Model.Yard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class YardDAOImpl implements YardDAO {
    private static Connection connection;
    private static YardDAOImpl instance;

    public YardDAOImpl() {
        Database db = new Database();
        connection = db.connect();
    }

    public static YardDAOImpl getInstance(){
        if (instance == null) {
            instance = new YardDAOImpl();
        }
        return instance;
    }

    @Override
    public boolean createYard(Yard yard) {
        String query = "INSERT INTO yard (yardName, yardLocation, yardCapacity, surfaceType, pricePerDay, description) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, yard.getYardName(),
                                    yard.getYardLocation(),
                                    yard.getYardCapacity(),
                                    yard.getSurfaceType(),
                                    yard.getPricePerDay(),
                                    yard.getDescription());
    }

    @Override
    public Yard getYardById(int yardId) {
        String query = "SELECT * FROM yard WHERE yardId = ?";
        return executeQuery(query, yardId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateYard(Yard yard) {
        String query = "UPDATE yard SET yardName = ?, yardLocation = ?, yardCapacity = ?, surfaceType = ?, pricePerDay = ?, description = ? WHERE yardId = ?";
        return executeUpdate(query, yard.getYardName(),
                                    yard.getYardLocation(),
                                    yard.getYardCapacity(),
                                    yard.getSurfaceType(),
                                    yard.getPricePerDay(),
                                    yard.getDescription(),
                                    yard.getYardId());
    }

    @Override
    public boolean deleteYard(int yardId) {
        String query = "DELETE FROM yard WHERE yardId = ?";
        return executeUpdate(query, yardId);
    }

    @Override
    public List<Yard> getAllYards() {
        String query = "SELECT * FROM yard";
        return executeQuery(query);
    }

    @Override
    public List<Yard> getYardsWithFilter(Integer minCapacity, Integer maxCapacity, String location, String surfaceType, Double minPrice, Double maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT * FROM yard WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        if (minCapacity != null) {
            sql.append(" AND yardCapacity >= ?");
            parameters.add(minCapacity);
        }
        if (maxCapacity != null) {
            sql.append(" AND yardCapacity <= ?");
            parameters.add(maxCapacity);
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND yardLocation = ?");
            parameters.add(location);
        }
        if (surfaceType != null && !surfaceType.isEmpty()) {
            sql.append(" AND surfaceType = ?");
            parameters.add(surfaceType);
        }
        if (minPrice != null) {
            sql.append(" AND pricePerDay >= ?");
            parameters.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND pricePerDay <= ?");
            parameters.add(maxPrice);
        }

        return executeQuery(sql.toString(), parameters.toArray());
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
    private List<Yard> executeQuery(String query, Object... params) {
        List<Yard> yards = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    yards.add(yardRowMapper(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yards;
    }

    // Method for setting query parameters (generic for any select query)
    private void setQueryParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    // Method to map a ResultSet row to a Yard object
    private Yard yardRowMapper(ResultSet resultSet) throws SQLException {
        return new Yard(
                resultSet.getInt("yardId"),
                resultSet.getString("yardName"),
                resultSet.getString("yardLocation"),
                resultSet.getInt("yardCapacity"),
                resultSet.getString("surfaceType"),
                resultSet.getDouble("pricePerDay"),
                resultSet.getString("description")
        );
    }
}


