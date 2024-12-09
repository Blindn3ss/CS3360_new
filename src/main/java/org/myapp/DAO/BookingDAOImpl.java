package org.myapp.DAO;

import org.myapp.Model.Booking;
import org.myapp.Model.BookingStatus;
import org.myapp.Database.Database;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@SuppressWarnings("CallToPrintStackTrace")
public class BookingDAOImpl implements BookingDAO {
    private static Connection connection;
    private static BookingDAOImpl instance;

    public BookingDAOImpl() {
        Database db = new Database();
        connection = db.connect();
    }

    public static BookingDAOImpl getInstance(){
        if (instance == null) {
            instance = new BookingDAOImpl();
        }
        return instance;
    }
    @Override
    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO booking (customerId, yardId, bookingDate, bookingPrice, bookingStatus) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(query, booking.getCustomerId(), booking.getYardId(), booking.getBookingDate(), booking.getBookingPrice(), booking.getBookingStatus().name());
    }

    @Override
    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM booking WHERE bookingId = ?";
        return executeQuery(query, bookingId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean updateBooking(Booking booking) {
        String query = "UPDATE booking SET customerId = ?, yardId = ?, bookingDate = ?, bookingPrice = ?, bookingStatus = ? WHERE bookingId = ?";
        return executeUpdate(query, booking.getCustomerId(),
                                    booking.getYardId(),
                                    booking.getBookingDate(),
                                    booking.getBookingPrice(),
                                    booking.getBookingStatus().name(),
                                    booking.getBookingId());
    }

    @Override
    public boolean updateBookingWithConnection(Connection connection, Booking booking) {
        String query = "UPDATE booking SET customerId = ?, yardId = ?, bookingDate = ?, bookingPrice = ?, bookingStatus = ? WHERE bookingId = ?";
        return executeUpdateWithConnection(connection, query, booking.getCustomerId(),
                booking.getYardId(),
                booking.getBookingDate(),
                booking.getBookingPrice(),
                booking.getBookingStatus().name(),
                booking.getBookingId());
    }

    @Override
    public boolean updateBookingWithStatus(int bookingId, BookingStatus status) {
        String query = "UPDATE booking SET bookingStatus = ? WHERE bookingId = ?";
        return executeUpdate(query, status.name(),
                                    bookingId);
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
    public Booking getBookedYardByDate(int yardId, LocalDate date) {
        String query = "SELECT * FROM booking WHERE yardId = ? AND bookingDate = ? AND bookingStatus = 'CONFIRMED'";
        return executeQuery(query, yardId, Date.valueOf(date)).stream().findFirst().orElse(null);
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        String query = "SELECT * FROM booking WHERE bookingStatus = ?";
        return executeQuery(query, status.name());
    }

    @Override
    public List<Booking> getBookingsByCustomerIdAndStatus(int customerId, BookingStatus status){
        String query = "SELECT * FROM booking WHERE customerId = ? AND bookingStatus = ?";
        return executeQuery(query, customerId, status.name());

    }

    // Note: If can not use generalize, then manually do from beginning (nkvd)
    @Override
    public boolean isBookingValidToCanCel(int bookingId, int customerId) {
        String query = "SELECT EXISTS (" +
                "SELECT 1 " +
                "FROM booking " +
                "WHERE bookingId = ? " +
                "AND customerId = ? " +
                "AND bookingStatus IN ('PENDING', 'CONFIRMED')" +
                ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, bookingId, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<Integer, List<Booking>> getBookingsForYards(List<Integer> yardIds) {
        Map<Integer, List<Booking>> bookingsMap = new LinkedHashMap<>();
        String query = "SELECT * FROM booking WHERE yardId = ? AND bookingStatus IN ('PENDING', 'CONFIRMED')";

        for (int yardId : yardIds) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, yardId);
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Booking> bookings = new ArrayList<>();
                    while (rs.next()) {
                        bookings.add(new Booking(
                                rs.getInt("bookingId"),
                                rs.getInt("yardId"),
                                rs.getInt("customerId"),
                                rs.getDate("bookingDate").toLocalDate(),
                                rs.getDouble("bookingPrice"),
                                BookingStatus.valueOf(rs.getString("bookingStatus"))
                        ));
                    }
                    bookingsMap.put(yardId, bookings);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return bookingsMap;
    }

    @Override
    public List<Booking> getPendingBookingOfYardInDate(int yardId, String date) {
        String query = "SELECT * FROM booking WHERE yardId = ? AND bookingDate = ? AND bookingStatus = 'PENDING'";
        LocalDate localDate = LocalDate.parse(date);
        Date sqlDate = Date.valueOf(localDate); // just to make sure
        return executeQuery(query, yardId, sqlDate);
    }

    @Override
    public double getRevenueOfYardInMonth(int yardId, int month, int year) {
        String query = "SELECT SUM(bookingPrice) AS totalRevenue " +
                "FROM booking " +
                "WHERE yardId = ? AND MONTH(bookingDate) = ? AND YEAR(bookingDate) = ? AND bookingStatus = 'COMPLETED'";

        double totalRevenue = 0.0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, yardId);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);  // Added year filter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalRevenue = resultSet.getDouble("totalRevenue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
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

    private boolean executeUpdateWithConnection(Connection connection, String query, Object... params) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setQueryParameters(preparedStatement, params);  // Assuming this method properly sets the query parameters
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
                resultSet.getDouble("bookingPrice"),
                BookingStatus.valueOf(resultSet.getString("bookingStatus")) // Convert database string to enum
        );
    }


}

