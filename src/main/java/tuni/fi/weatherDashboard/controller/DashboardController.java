package tuni.fi.weatherDashboard.controller;

import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;
import tuni.fi.weatherDashboard.service.WeatherApiService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;

public class DashboardController extends BaseController {
    @FXML private TextField locationField;
    @FXML private ComboBox<String> regionComboBox;
    @FXML private ComboBox<String> dateRangeComboBox;
    @FXML private CheckBox showTemperatureCheck;
    @FXML private CheckBox showHumidityCheck;
    @FXML private Button searchButton;
    @FXML private LineChart<String, Number> weatherChart;
    @FXML private Label temperatureLabel;
    @FXML private Label humidityLabel;
    @FXML private Label descriptionLabel;

    private WeatherApiService weatherService;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        weatherService = new WeatherApiService();
        setupInitialState();
        
        // Add debug prints to verify initialization
        System.out.println("DashboardController initialized");
        System.out.println("Location field null? " + (locationField == null));
        System.out.println("Search button null? " + (searchButton == null));
    }

    private void setupInitialState() {
        if (regionComboBox != null) {
            regionComboBox.getItems().addAll("Europe", "North America", "Asia");
        }
        if (dateRangeComboBox != null) {
            dateRangeComboBox.getItems().addAll("Today", "Week", "Month");
        }
        
        if (showTemperatureCheck != null) {
            showTemperatureCheck.setSelected(true);
        }
        if (showHumidityCheck != null) {
            showHumidityCheck.setSelected(true);
        }
        
        if (weatherChart != null) {
            weatherChart.setTitle("Weather Data");
            weatherChart.getXAxis().setLabel("Time");
            weatherChart.getYAxis().setLabel("Value");
        }

        if (searchButton != null) {
            searchButton.setOnAction(event -> {
                System.out.println("Search button clicked");
                handleSearch();
            });
        }

        // Add listener to location field for enter key
        if (locationField != null) {
            locationField.setOnAction(event -> handleSearch());
        }
    }

    @FXML
    private void handleSearch() {
        if (locationField == null) {
            System.err.println("Location field is null!");
            return;
        }

        String location = locationField.getText();
        System.out.println("Searching for location: " + location);

        if (location == null || location.trim().isEmpty()) {
            showAlert("Error", "Please enter a location");
            return;
        }

        try {
            System.out.println("Fetching weather data...");
            DataModel weatherData = weatherService.fetchWeatherData(location.trim());
            updateUI(weatherData);
        } catch (Exception e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
            showAlert("Error", "Failed to fetch weather data: " + e.getMessage());
        }
    }

    private void updateUI(DataModel dataModel) {
        if (dataModel == null || dataModel.getWeatherDataList().isEmpty()) {
            System.out.println("No weather data available");
            return;
        }

        Platform.runLater(() -> {
            try {
                WeatherData currentWeather = dataModel.getWeatherDataList().get(0);
                
                // Update labels with null checks
                if (temperatureLabel != null) {
                    temperatureLabel.setText(String.format("Temperature: %.1f°C", 
                        currentWeather.getTemperature()));
                }
                if (humidityLabel != null) {
                    humidityLabel.setText(String.format("Humidity: %.1f%%", 
                        currentWeather.getHumidity()));
                }
                if (descriptionLabel != null && currentWeather.getDescription() != null) {
                    descriptionLabel.setText("Conditions: " + currentWeather.getDescription());
                }

                // Update chart
                updateChart(dataModel);
                
                System.out.println("UI updated successfully");
            } catch (Exception e) {
                System.err.println("Error updating UI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void updateChart(DataModel dataModel) {
        if (weatherChart == null) {
            System.err.println("Weather chart is null!");
            return;
        }

        weatherChart.getData().clear();

        if (showTemperatureCheck != null && showTemperatureCheck.isSelected()) {
            XYChart.Series<String, Number> tempSeries = new XYChart.Series<>();
            tempSeries.setName("Temperature");
            dataModel.getWeatherDataList().forEach(data -> 
                tempSeries.getData().add(new XYChart.Data<>(
                    data.getDateTime().toString(), 
                    data.getTemperature()
                ))
            );
            weatherChart.getData().add(tempSeries);
        }

        if (showHumidityCheck != null && showHumidityCheck.isSelected()) {
            XYChart.Series<String, Number> humiditySeries = new XYChart.Series<>();
            humiditySeries.setName("Humidity");
            dataModel.getWeatherDataList().forEach(data -> 
                humiditySeries.getData().add(new XYChart.Data<>(
                    data.getDateTime().toString(), 
                    data.getHumidity()
                ))
            );
            weatherChart.getData().add(humiditySeries);
        }
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}