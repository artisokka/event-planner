package tuni.fi.weatherDashboard.controller;

import java.util.List;
import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;
import tuni.fi.weatherDashboard.service.WeatherApiService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.application.Platform;
import tuni.fi.weatherDashboard.model.AirQualityData;
import tuni.fi.weatherDashboard.service.OpenAQService;
import tuni.fi.weatherDashboard.utils.AlertUtils;

public class DashboardController extends BaseController {

    @FXML private TextField locationField;
    @FXML private ComboBox<String> regionComboBox;
    @FXML private ComboBox<String> dateRangeComboBox;
    @FXML private CheckBox showTemperatureCheck;
    @FXML private CheckBox showHumidityCheck;
    @FXML private Button searchButton;
    @FXML private Button saveButton;
    @FXML private LineChart<String, Number> weatherChart;
    @FXML private Label temperatureLabel;
    @FXML private Label humidityLabel;
    @FXML private Label descriptionLabel;
    @FXML private ListView weatherSummaryListView;
    @FXML private LineChart<String, Number> airQualityChart;

    private WeatherApiService weatherService;
    private OpenAQService airQualityService;
    private HistoryController historyController;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        weatherService = new WeatherApiService();
        airQualityService = new OpenAQService();

        setupInitialState();

        // Add debug prints to verify initialization
        System.out.println("DashboardController initialized");
        System.out.println("HistoryController initialized");
        System.out.println("Location field null? " + (locationField == null));
        System.out.println("Search button null? " + (searchButton == null));

        this.historyController = ControllerHolder.getHistoryController();
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

        if (saveButton != null) {
            saveButton.setOnAction(event -> {
                System.out.println("Save button clicked");
                handleSave();
            });
        }

        // Add listener to location field for enter key
        if (locationField != null) {
            locationField.setOnAction(event -> handleSearch());
        }
    }

    private void saveSearchToHistory(WeatherData weatherData) {
        if (this.historyController == null) {
            System.out.println(
                    "Can't add search to history: historyController is null");
        } else {
            this.historyController.addSearchToHistory(weatherData);
            System.out.println("Adding to history: " + weatherData);
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
            AlertUtils.showAlert("Error", "Please enter a location");
            return;
        }

        try {
            // Fetch weather data
            System.out.println("Fetching weather data...");
            DataModel weatherDataModel = weatherService.fetchWeatherData(
                    location.trim());
            List<WeatherData> forecastData = weatherDataModel.
                    getWeatherDataList();

            if (forecastData.isEmpty()) {
                AlertUtils.showAlert("Info",
                        "No weather data available for the given location.");
                return;
            }

            // Fetch air quality data
            System.out.println("Fetching air quality data...");
            airQualityService.fetchAirQualityData(location);
            //AirQualityData airQualityData = airQualityService.fetchAirQualityData(location.trim());

            // Save to history
            WeatherData currentWeather = forecastData.get(0);
            saveSearchToHistory(currentWeather);

            // Update UI
            //updateUI(weatherDataModel, airQualityData);

        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
            AlertUtils.showAlert("Error", "Failed to fetch data: " + e.
                    getMessage());
        }
    }


    @FXML
    private void handleSave() {
        if (locationField == null) {
            System.err.println("Location field is null!");
            return;
        }

        String location = locationField.getText();
        if (location == null || location.trim().isEmpty()) {
            AlertUtils.showAlert("Error", "Please enter a location to save");
            return;
        }

        location = location.trim();

        // Save the location to SavePreferencesController
        SavePreferencesController preferencesController = ControllerHolder.
                getPreferencesController();
        preferencesController.addLocation(location);
    }

    private void updateUI(DataModel dataModel, AirQualityData airQualityData) {
        if (dataModel == null || dataModel.getWeatherDataList().isEmpty()) {
            System.out.println("No weather data available");
            return;
        }

        Platform.runLater(() -> {
            try {
                WeatherData currentWeather = dataModel.getWeatherDataList().get(
                        0);

                // Update weather labels
                if (temperatureLabel != null) {
                    temperatureLabel.setText(String.
                            format("Temperature: %.1f°C", currentWeather.
                                    getTemperature()));
                }
                if (humidityLabel != null) {
                    humidityLabel.setText(String.format("Humidity: %.1f%%",
                            currentWeather.getHumidity()));
                }
                if (descriptionLabel != null &&
                        currentWeather.getDescription() != null) {
                    descriptionLabel.setText("Conditions: " + currentWeather.
                            getDescription());
                }

                // Update weather summary ListView for current weather
                updateWeatherSummaryListView(currentWeather);

                // Update air quality summary
                //if (airQualityData != null) {
                //    updateAirQualityChart(airQualityData);
                //}

                // Update weather chart
                updateChart(dataModel);

                System.out.println("UI updated successfully");
            } catch (Exception e) {
                System.err.println("Error updating UI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void updateWeatherSummaryListView(WeatherData currentWeather) {
        if (weatherSummaryListView == null) {
            return;
        }

        // Clear previous data in the list view
        weatherSummaryListView.getItems().clear();

        // Add current weather summary to the list view
        weatherSummaryListView.getItems().add(String.format(
                "Temperature: %.1f°C",
                currentWeather.
                        getTemperature()));
        weatherSummaryListView.getItems().add(String.format("Humidity: %.1f%%",
                currentWeather.
                        getHumidity()));
        if (currentWeather.getDescription() != null) {
            weatherSummaryListView.getItems().add("Conditions: " +
                    currentWeather.getDescription());
        }
    }

    private void updateAirQualityChart(AirQualityData airQualityData) {
        if (airQualityChart == null) {
            System.err.println("Air Quality Chart is null!");
            return;
        }

        airQualityChart.getData().clear();

        if (airQualityData != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Air Quality");

            series.getData().add(new XYChart.Data<>("PM2.5", airQualityData.
                    getPm25()));
            series.getData().add(new XYChart.Data<>("PM10", airQualityData.
                    getPm10()));
            series.getData().add(new XYChart.Data<>("NO2", airQualityData.
                    getNo2()));

            airQualityChart.getData().add(series);
        }
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
            XYChart.Series<String, Number> humiditySeries =
                    new XYChart.Series<>();
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
}
