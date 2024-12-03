package org.myapp.Menu;

import org.myapp.DAO.BookingDAOImpl;
import org.myapp.Database.Database;
import org.myapp.Model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static boolean doesUsernameExist(String username) throws SQLException {
        String query =
                "SELECT COUNT(*) FROM customer WHERE username = ? " +
                        "UNION " +
                        "SELECT COUNT(*) FROM manager WHERE username = ?";

        Connection conn = new Database().connect();
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int count = 0;

                // Iterate over the result set to sum counts from both tables
                while (resultSet.next()) {
                    count += resultSet.getInt(1);
                }

                // If the count is greater than 0, the username exists in either of the tables
                return count > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return false;
    }

    public static boolean isValidDateFormat(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return true;  // Valid date format
        } catch (DateTimeParseException e) {
            return false;  // Invalid date format
        }
    }

    public static boolean isBeforeCurrentDate(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        return date.isBefore(currentDate);
    }

    public static boolean bookingExist(int yardId, LocalDate date){
        return BookingDAOImpl.getInstance().getBookedYardByDate(yardId, date) != null;
    }
}