package tuni.fi.weatherDashboard.utils;

/**
 *
 * @author janik
 */
public class StringUtils {

    public static String capitalizeCities(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split("\\s+"); // Split by whitespace
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                // Convert first letter to uppercase and the rest to lowercase
                String capitalizedWord = word.substring(0, 1).toUpperCase() +
                        word.substring(1).toLowerCase();
                result.append(capitalizedWord).append(" ");
            }
        }

        // Trim and return
        return result.toString().trim();
    }
}
