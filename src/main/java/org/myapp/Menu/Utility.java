package org.myapp.Menu;

import org.myapp.DAO.BookingDAOImpl;
import org.myapp.Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static LocalDate latestAlowedDate = LocalDate.now().plusDays(14);

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
        // don't know if it is a good query enough?
        Connection conn = new Database().connect();
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int count = 0;
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
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isInvalidDateForBooking(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        return date.isBefore(currentDate) || date.isAfter(latestAlowedDate);
    }

    public static boolean bookingExist(int yardId, LocalDate date){
        return BookingDAOImpl.getInstance().getBookedYardByDate(yardId, date) != null;
    }

    public static String wrapText(String text, int maxLineLength) {
        StringBuilder wrappedText = new StringBuilder(); // faster
        String[] words = text.split(" ");
        int currentLineLength = 0;

        for (String word : words) {
            // If adding the word exceeds the max line length, start a new line
            if (currentLineLength + word.length() + 1 > maxLineLength) {
                wrappedText.append("\n");
                currentLineLength = 0;
            }
            // If it's not the first word in a new line, add a space
            if (currentLineLength > 0) {
                wrappedText.append(" ");
                currentLineLength++;
            }//-? Can be more dynamic  ???
            // Add the word to the line
            wrappedText.append(word);
            currentLineLength += word.length();
        }

        return wrappedText.toString();
    }
}
