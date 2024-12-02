package org.myapp.DAO;

import org.myapp.Database.Database;
import org.myapp.Model.TimeSlot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class TimeSlotDAOImpl implements TimeSlotDAO {
    private final Connection connection;

    public TimeSlotDAOImpl() {
        Database db = new Database();
        connection = db.connect();
    }

    @Override
    public boolean createTimeSlot(TimeSlot timeSlot) {
        String query = "INSERT INTO timeSlot (yardId, startTime, endTime) VALUES (?, ?, ?)";
        return executeUpdate(query, timeSlot);
    }

    @Override
    public TimeSlot getTimeSlotById(int timeSlotId) {
        String query = "SELECT * FROM timeSlot WHERE timeSlotId = ?";
        return executeQuery(query, timeSlotId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateTimeSlot(TimeSlot timeSlot) {
        String query = "UPDATE timeSlot SET yardId = ?, startTime = ?, endTime = ? WHERE timeSlotId = ?";
        return executeUpdate(query, timeSlot);
    }

    @Override
    public boolean deleteTimeSlot(int timeSlotId) {
        String query = "DELETE FROM timeSlot WHERE timeSlotId = ?";
        return executeUpdate(query, timeSlotId);
    }

    @Override
    public List<TimeSlot> getAllTimeSlots() {
        String query = "SELECT * FROM timeSlot";
        return executeQuery(query);
    }

    @Override
    public List<TimeSlot> getTimeSlotsByYardId(int yardId) {
        String query = "SELECT * FROM timeSlot WHERE yardId = ?";
        return executeQuery(query, yardId);
    }

    private boolean executeUpdate(String query, TimeSlot timeSlot) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setTimeSlotParameters(preparedStatement, timeSlot);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean executeUpdate(String query, int timeSlotId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, timeSlotId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<TimeSlot> executeQuery(String query, Object... params) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    timeSlots.add(timeSlotRowMapper(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timeSlots;
    }

    private void setQueryParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    private void setTimeSlotParameters(PreparedStatement preparedStatement, TimeSlot timeSlot) throws SQLException {
        preparedStatement.setInt(1, timeSlot.getYardId());
        preparedStatement.setTime(2, Time.valueOf(timeSlot.getStartTime()));
        preparedStatement.setTime(3, Time.valueOf(timeSlot.getEndTime()));
    }

    private TimeSlot timeSlotRowMapper(ResultSet resultSet) throws SQLException {
        return new TimeSlot(
                resultSet.getInt("timeSlotId"),
                resultSet.getInt("yardId"),
                resultSet.getTime("startTime").toLocalTime(),
                resultSet.getTime("endTime").toLocalTime()
        );
    }

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

