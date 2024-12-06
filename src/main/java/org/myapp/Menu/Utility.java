package org.myapp.Menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static LocalDate latestAlowedDate = LocalDate.now().plusDays(14);

    public static boolean isValidEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
