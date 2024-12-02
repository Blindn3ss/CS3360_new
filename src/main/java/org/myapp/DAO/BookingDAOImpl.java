package org.myapp.DAO;

import org.myapp.Model.Booking;
import org.myapp.Model.BookingStatus;
import org.myapp.Database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class BookingDAOImpl implements BookingDAO {
    private final Connection connection;

    public BookingDAOImpl() {
        Database db = new Database();
        connection = db.connect();
    }

    @Override
    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO booking (customerId, yardId, bookingDate, totalPrice, bookingStatus) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(query, booking.getCustomerId(), booking.getYardId(), booking.getBookingDate(), booking.getTotalPrice(), booking.getBookingStatus().name());
    }

    @Override
    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM booking WHERE bookingId = ?";
        return executeQuery(query, bookingId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateBooking(Booking booking) {
        String query = "UPDATE booking SET customerId = ?, yardId = ?, bookingDate = ?, totalPrice = ?, bookingStatus = ? WHERE bookingId = ?";
        return executeUpdate(query, booking.getCustomerId(), booking.getYardId(), booking.getBookingDate(), booking.getTotalPrice(), booking.getBookingStatus().name(), booking.getBookingId());
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        String query = "DELETE FROM booking WHERE bookingId = ?";
        return executeUpdate(query, bookingId);
    }

    @Override
    public List<Booking> getAllBookings() {
        String query = "SELECT * FROM booking";
        return executeQuery(query);
    }

    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) {
        String query = "SELECT * FROM booking WHERE customerId = ?";
        return executeQuery(query, customerId);
    }

    @Override
    public List<Booking> getBookingsByYardId(int yardId) {
        String query = "SELECT * FROM booking WHERE yardId = ?";
        return executeQuery(query, yardId);
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        String query = "SELECT * FROM booking WHERE bookingStatus = ?";
        return executeQuery(query, status.name()); // Convert the enum to a string for the query
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
    private List<Booking> executeQuery(String query, Object... params) {
        List<Booking> bookings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(bookingRowMapper(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Method for setting query parameters (generic for any select or update query)
    private void setQueryParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    // Method to map a ResultSet row to a Booking object
    private Booking bookingRowMapper(ResultSet resultSet) throws SQLException {
        return new Booking(
                resultSet.getInt("bookingId"),
                resultSet.getInt("customerId"),
                resultSet.getInt("yardId"),
                resultSet.getDate("bookingDate").toLocalDate(),
                resultSet.getDouble("totalPrice"),
                BookingStatus.valueOf(resultSet.getString("bookingStatus")) // Convert database string to enum
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

