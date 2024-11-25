package tuni.fi.weatherDashboard.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import tuni.fi.weatherDashboard.service.WeatherApiService;
import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;
import javafx.application.Platform;

import java.util.prefs.Preferences;

public class SavePreferencesController extends BaseController {

    @FXML private GridPane preferencesGrid;
    @FXML private ListView<String> savedLocationsList;
    @FXML private Button removeButton;
    @FXML private Button clearAllButton;

    private Preferences userPreferences;
    private WeatherApiService weatherService;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        weatherService = new WeatherApiService();
        userPreferences = Preferences.userNodeForPackage(this.getClass());
        loadSavedLocations();

        if (removeButton != null) {
            removeButton.setOnAction(event -> handleRemoveSelectedLocation());
        }

        if (clearAllButton != null) {
            clearAllButton.setOnAction(event -> handleClearAllLocations());
        }
    }

    private void loadSavedLocations() {
        String savedLocs = userPreferences.get("savedLocations", "");
        if (!savedLocs.isEmpty()) {
            String[] locations = savedLocs.split(",");
            savedLocationsList.getItems().setAll(locations);  // Rebuild ListView items
            updateWeatherForLocations(locations);
        } else {
            // If no locations are saved, clear the weather grid
            preferencesGrid.getChildren().clear();
        }
    }

    private void updateWeatherForLocations(String[] locations) {
        // Clear the grid first to avoid leftover data
        preferencesGrid.getChildren().clear();

        for (int i = 0; i < locations.length && i < 6; i++) {
            final String location = locations[i];
            final int index = i;
            try {
                // Fetch weather data for the location
                DataModel weatherData = weatherService.fetchWeatherData(
                        location.trim());
                WeatherData currentWeather = weatherData.getWeatherDataList().
                        get(0);

                String weatherInfo = String.format(
                        "Temp: %.1f°C, Humidity: %.1f%%",
                        currentWeather.getTemperature(), currentWeather.
                        getHumidity());

                // Create a label to display the weather info and add it to the grid
                Label weatherLabel = new Label(location + "\n" + weatherInfo);
                preferencesGrid.add(weatherLabel, 0, i);

                // Create a trash can button to remove the weather info and add it to the grid
                Button removeButton = new Button("🗑️");
                removeButton.setOnAction(event -> handleRemoveLocation(
                        location)); // Pass final location
                preferencesGrid.add(removeButton, 1, i);

            } catch (Exception e) {
                // Handle errors in case of failed weather data fetch
                Label errorLabel = new Label(location +
                        "\nError fetching weather");
                preferencesGrid.add(errorLabel, 0, i);
            }
        }
    }
    private void handleRemoveLocation(String location) {
        // Remove from ListView
        savedLocationsList.getItems().remove(location);

        // Remove from preferences
        String savedLocs = userPreferences.get("savedLocations", "");
        String updatedLocs = savedLocs.replace(location + ",", "").replace(
                location, "");
        userPreferences.put("savedLocations", updatedLocs);

        // Reload saved locations and update the grid
        loadSavedLocations();
    }

    /**
     * Handle the removal of the selected location from the ListView.
     */
    private void handleRemoveSelectedLocation() {
        String selectedLocation = savedLocationsList.getSelectionModel().
                getSelectedItem();
        if (selectedLocation != null) {
            savedLocationsList.getItems().remove(selectedLocation);
            saveLocationsToPreferences(); // Update preferences after removal
            loadSavedLocations(); // Reload saved locations and refresh grid
        }
    }

    /**
     * Handle clearing all saved locations from the ListView and preferences.
     */
    private void handleClearAllLocations() {
        savedLocationsList.getItems().clear(); // Clear ListView
        userPreferences.remove("savedLocations"); // Clear preferences
        preferencesGrid.getChildren().clear(); // Clear the weather grid
    }

    /**
     * Save the current list of locations back to preferences.
     */
    private void saveLocationsToPreferences() {
        StringBuilder locationsBuilder = new StringBuilder();
        for (String location : savedLocationsList.getItems()) {
            if (locationsBuilder.length() > 0) {
                locationsBuilder.append(",");
            }
            locationsBuilder.append(location);
        }
        userPreferences.put("savedLocations", locationsBuilder.toString());
    }
}
