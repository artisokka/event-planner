package tuni.fi.weatherDashboard.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
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

    @FXML private Label location1Label;
    @FXML private Label location2Label;
    @FXML private Label location3Label;
    @FXML private Label location4Label;
    @FXML private Label location5Label;
    @FXML private Label location6Label;

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
            savedLocationsList.getItems().addAll(locations);
            updateWeatherForLocations(locations);
        }
    }

    private void updateWeatherForLocations(String[] locations) {
        // Fetch and display weather for each location in the saved list
        for (int i = 0; i < locations.length && i < 6; i++) {
            String location = locations[i];
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

                // Update the corresponding label
                updateLabel(i, location, weatherInfo);
            } catch (Exception e) {
                // Handle errors in case of failed weather data fetch
                updateLabel(i, location, "Error fetching weather");
            }
        }
    }

    private void updateLabel(int index, String location, String weatherInfo) {
        Platform.runLater(() -> {
            String labelText = location + "\n" + weatherInfo;
            switch (index) {
                case 0:
                    location1Label.setText(labelText);
                    break;
                case 1:
                    location2Label.setText(labelText);
                    break;
                case 2:
                    location3Label.setText(labelText);
                    break;
                case 3:
                    location4Label.setText(labelText);
                    break;
                case 4:
                    location5Label.setText(labelText);
                    break;
                case 5:
                    location6Label.setText(labelText);
                    break;
            }
        });
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

    /**
     * Handle the removal of the selected location from the ListView.
     */
    private void handleRemoveSelectedLocation() {
        String selectedLocation = savedLocationsList.getSelectionModel().
                getSelectedItem();
        if (selectedLocation != null) {
            savedLocationsList.getItems().remove(selectedLocation);
            saveLocationsToPreferences(); // Update preferences after removal
        }
    }

    /**
     * Handle clearing all saved locations from the ListView and preferences.
     */
    private void handleClearAllLocations() {
        savedLocationsList.getItems().clear();
        userPreferences.remove("savedLocations"); // Clear preferences
        clearGridLabels(); // Clear all labels in the grid
    }

    private void clearGridLabels() {
        location1Label.setText("");
        location2Label.setText("");
        location3Label.setText("");
        location4Label.setText("");
        location5Label.setText("");
        location6Label.setText("");
    }
}
