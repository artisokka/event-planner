package tuni.fi.weatherDashboard.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.prefs.Preferences;
import javafx.scene.control.Label;
import tuni.fi.weatherDashboard.utils.AlertUtils;
import tuni.fi.weatherDashboard.service.WeatherApiService;
import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;

public class SavePreferencesController extends BaseController {

    @FXML private ListView<String> savedLocationsList;
    @FXML private GridPane preferencesGrid;
    private ObservableList<String> savedLocations;

    private Preferences userPreferences;
    private WeatherApiService weatherService;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        userPreferences = Preferences.userNodeForPackage(this.getClass());
        weatherService = new WeatherApiService(); // Initialize weather service

        savedLocations = FXCollections.observableArrayList();
        savedLocationsList.setItems(savedLocations); // Bind ListView to ObservableList

        loadSavedLocations();
    }

    private void loadSavedLocations() {
        String savedLocs = userPreferences.get("savedLocations", "");
        if (!savedLocs.isEmpty()) {
            String[] locations = savedLocs.split(",");
            savedLocations.setAll(locations); // Update the ObservableList with loaded data
            displayWeatherCards();
        }
    }

    private void displayWeatherCards() {
        preferencesGrid.getChildren().clear(); // Clear existing content

        int row = 0;
        int col = 0;

        for (int i = 0; i < savedLocations.size(); i++) {
            String location = savedLocations.get(i);
            VBox weatherCard = createWeatherCard(location);
            preferencesGrid.add(weatherCard, col, row);

            // Move to next column or row if the column limit is reached
            if (col == 1) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
    }

    private VBox createWeatherCard(String location) {
        VBox weatherCard = new VBox();
        weatherCard.setSpacing(10);
        weatherCard.setStyle(
                "-fx-background-color: #D0E7F9; -fx-padding: 10; -fx-border-radius: 8;");

        // Fetch weather data asynchronously
        fetchWeatherDataForLocation(location, weatherCard);

        return weatherCard;
    }

    private void fetchWeatherDataForLocation(String location, VBox weatherCard) {
        new Thread(() -> {
            try {
                DataModel weatherDataModel = weatherService.fetchWeatherData(
                        location);
                if (weatherDataModel != null && !weatherDataModel.
                        getWeatherDataList().isEmpty()) {
                    WeatherData currentWeather = weatherDataModel.
                            getWeatherDataList().get(0);

                    // Update UI with weather data on the JavaFX application thread
                    Platform.runLater(() -> {
                        Label locationLabel = new Label(location);
                        locationLabel.setFont(Font.font("Arial", 16));

                        Text tempLabel = new Text(String.format(
                                "Temperature: %.1f°C", currentWeather.
                                        getTemperature()));
                        tempLabel.setFont(Font.font("Arial", 14));

                        Text humidityLabel = new Text(String.format(
                                "Humidity: %.1f%%", currentWeather.
                                        getHumidity()));
                        humidityLabel.setFont(Font.font("Arial", 14));

                        ImageView icon = new ImageView(
                                "/img/sunny.png");  // Placeholder for icon
                        icon.setFitWidth(50);
                        icon.setFitHeight(50);

                        weatherCard.getChildren().addAll(locationLabel,
                                tempLabel, humidityLabel, icon);
                    });
                }
            } catch (Exception e) {
                System.err.println(
                        "Error fetching weather data for location: " + location);
                e.printStackTrace();
            }
        }).start();
    }

    public void saveLocations() {
        StringBuilder locationsBuilder = new StringBuilder();
        for (String location : savedLocations) {
            if (locationsBuilder.length() > 0) {
                locationsBuilder.append(",");
            }
            locationsBuilder.append(location);
        }
        userPreferences.put("savedLocations", locationsBuilder.toString());
    }

    public void addLocation(String location) {
        if (!savedLocations.contains(location)) {
            savedLocations.add(location); // ObservableList updates automatically
            saveLocations();
            displayWeatherCards();  // Update the UI with the new location
        } else {
            AlertUtils.showAlert("Info", "Location is already saved");
        }
    }

    public void removeSelectedLocation() {
        String selectedLocation = savedLocationsList.getSelectionModel().
                getSelectedItem();
        if (selectedLocation != null) {
            savedLocations.remove(selectedLocation); // ObservableList updates automatically
            saveLocations(); // Persist the updated list
            displayWeatherCards();  // Update the UI with the removed location
            AlertUtils.showAlert("Success", "Location removed successfully");
        } else {
            AlertUtils.showAlert("Error", "No location selected to remove");
        }
    }

    public void removeAllLocations() {
        savedLocations.clear(); // ObservableList updates automatically
        saveLocations(); // Save the empty list
        displayWeatherCards(); // Clear the weather cards from UI
        AlertUtils.showAlert("Success", "All locations removed successfully");
    }

}
