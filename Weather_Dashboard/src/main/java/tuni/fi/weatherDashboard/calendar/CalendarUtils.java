package tuni.fi.weatherDashboard.calendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tuni.fi.weatherDashboard.model.WeatherData;

/**
 *
 * @author janik
 */
public class CalendarUtils {

    @FXML private GridPane calendarGrid;

    public void highlightCalendar(List<WeatherData> forecastData) {
        Map<LocalDate, String> highlightMap = new HashMap<>();

        // Find the warmest day without rain
        WeatherData bestDay = forecastData.stream()
                .filter(data -> data.getPrecipitation() < 1) // No rain
                .max(Comparator.comparingDouble(WeatherData::getTemperature)) // Max temperature
                .orElse(null);

        // Find the coldest day(s) with rain
        WeatherData worstDay = forecastData.stream()
                .filter(data -> data.getPrecipitation() >= 1) // Rain present
                .min(Comparator.comparingDouble(WeatherData::getTemperature)) // Min temperature
                .orElse(null);

        // Apply highlights to the calendar
        for (WeatherData data : forecastData) {
            LocalDate date = data.getDateTime().toLocalDate();

            if (bestDay != null && date.equals(bestDay.getDateTime().
                    toLocalDate())) {
                highlightMap.put(date, "best-weather");  // Highlight warmest day without rain
            } else if (worstDay != null && date.equals(worstDay.getDateTime().
                    toLocalDate())) {
                highlightMap.put(date, "worst-weather");  // Highlight coldest day with rain
            } else {
                highlightMap.put(date, "moderate-weather");  // Moderate days
            }
        }

        // Update the calendar UI with the highlight map
        updateCalendarUI(highlightMap);

        System.out.println("Tried to call updateCalendarUI");
    }

    public void updateCalendarUI(Map<LocalDate, String> highlightMap) {
        if (calendarGrid != null) {
            System.out.println("CalendarGrid isn't null");
            for (Node node : calendarGrid.getChildren()) {
                if (node instanceof StackPane stackPane) {
                    Label dayLabel = (Label) stackPane.getChildren().get(0);  // Get the Label from StackPane
                    String text = dayLabel.getText();
                    System.out.println("String text: " + text);
                    if (!text.isEmpty()) {
                        LocalDate cellDate = getDateFromCell(dayLabel);

                        // Remove previous dots (if any)
                        stackPane.getChildren().removeIf(child ->
                                child instanceof Circle);

                        if (cellDate != null && highlightMap.containsKey(
                                cellDate)) {
                            String styleClass = highlightMap.get(cellDate);

                            // Add the style class to the label (optional)
                            dayLabel.getStyleClass().add(styleClass);

                            // Add a colored dot based on the weather type
                            Circle dot = new Circle(5);  // Circle with radius 5px

                            switch (styleClass) {
                                case "best-weather":
                                    dot.setFill(Color.GREEN);  // Green dot for best weather
                                    break;
                                case "worst-weather":
                                    dot.setFill(Color.RED);  // Red dot for worst weather
                                    break;
                                case "moderate-weather":
                                    dot.setFill(Color.YELLOW);  // Yellow dot for moderate weather
                                    break;
                                default:
                                    dot.setFill(Color.TRANSPARENT);  // No dot for default
                                    break;
                            }

                            // Position the dot at the bottom right of the label
                            dot.setTranslateX(dayLabel.getWidth() - 10);  // 10px from right
                            dot.setTranslateY(dayLabel.getHeight() - 10);  // 10px from bottom

                            // Add the dot to the StackPane
                            stackPane.getChildren().add(dot);
                        }
                    }
                }
            }
        } else {
            System.out.println("CalendarGrid is null!!! sos");
        }
    }



    private void setDateForCell(Label cell, LocalDate date) {
        cell.setUserData(date); // Store date in user data.
    }

    private LocalDate getDateFromCell(Label cell) {
        Object userData = cell.getUserData();
        return userData instanceof LocalDate ? (LocalDate) userData : null;
    }
}
