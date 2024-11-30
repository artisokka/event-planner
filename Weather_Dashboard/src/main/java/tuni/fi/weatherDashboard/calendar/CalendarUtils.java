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
import tuni.fi.weatherDashboard.model.WeatherData;

/**
 *
 * @author janik
 */
public class CalendarUtils {

    @FXML private GridPane calendarGrid;

    public void highlightCalendar(List<WeatherData> forecastData) {
        Map<LocalDate, String> highlightMap = new HashMap<>();

        // Find best and worst days
        WeatherData bestDay = forecastData.stream()
                .filter(data -> data.getPrecipitation() < 1)
                .max(Comparator.comparingDouble(WeatherData::getTemperature))
                .orElse(null);

        WeatherData worstDay = forecastData.stream()
                .min(Comparator.comparingDouble(data ->
                        data.getTemperature() + (data.getPrecipitation() >= 1
                        ? 10 : 0)))
                .orElse(null);

        // Apply highlights
        for (WeatherData data : forecastData) {
            LocalDate date = data.getDateTime().toLocalDate();

            if (bestDay != null && date.equals(bestDay.getDateTime().
                    toLocalDate())) {
                highlightMap.put(date, "best-weather");
            } else if (worstDay != null && date.equals(worstDay.getDateTime().
                    toLocalDate())) {
                highlightMap.put(date, "worst-weather");
            } else {
                highlightMap.put(date, "moderate-weather");
            }
        }

        // Update the calendar UI
        updateCalendarUI(highlightMap);
    }

    public void updateCalendarUI(Map<LocalDate, String> highlightMap) {
        if (calendarGrid != null) {
            for (Node node : calendarGrid.getChildren()) {
                if (node instanceof Label dayLabel) {
                    String text = dayLabel.getText();
                    if (!text.isEmpty()) {
                        LocalDate cellDate = getDateFromCell(dayLabel);

                        if (cellDate != null && highlightMap.containsKey(
                                cellDate)) {
                            String styleClass = highlightMap.get(cellDate);
                            dayLabel.getStyleClass().add(styleClass);
                        } else {
                            // Reset styles if no highlight exists for the cell date.
                            dayLabel.getStyleClass().clear();
                        }
                    }
                }
            }
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
