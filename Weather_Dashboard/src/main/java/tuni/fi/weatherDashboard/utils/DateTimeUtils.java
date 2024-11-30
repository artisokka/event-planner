package tuni.fi.weatherDashboard.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author janik
 */
public class DateTimeUtils {
    public static String formatDateTime(LocalDateTime dateTime) {

        // Define the custom format pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd.MM. 'at' HH.mm");

        // Format the LocalDateTime
        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime;
    }

}
