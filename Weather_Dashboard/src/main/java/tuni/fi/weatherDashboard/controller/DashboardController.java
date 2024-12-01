package tuni.fi.weatherDashboard.controller;

import tuni.fi.weatherDashboard.calendar.CalendarUtils;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;
import tuni.fi.weatherDashboard.service.WeatherApiService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import tuni.fi.weatherDashboard.model.AirQualityData;
import tuni.fi.weatherDashboard.service.OpenAQService;
import tuni.fi.weatherDashboard.utils.AlertUtils;
import tuni.fi.weatherDashboard.utils.DateTimeUtils;
import tuni.fi.weatherDashboard.utils.LineChartUtils;
import tuni.fi.weatherDashboard.utils.StringUtils;

public class DashboardController extends BaseController {

    @FXML private TextField locationField;
    @FXML private ComboBox<String> regionComboBox, dateRangeComboBox;
    @FXML private CheckBox showTemperatureCheck, showHumidityCheck;
    @FXML private Button searchButton, saveButton;
    @FXML private LineChart<String, Number> weatherChart;
    @FXML private Label temperatureLabel, humidityLabel, descriptionLabel;
    @FXML private ListView<String> weatherSummaryListView;
    @FXML private BarChart<String, Number> airQualityChart;
    @FXML private GridPane calendarGrid;
    @FXML private Button prevMonthButton, nextMonthButton;
    @FXML private Label headerLabel;

    private WeatherApiService weatherService;
    private OpenAQService airQualityService;
    private HistoryController historyController;
    private CalendarUtils calendar;
    private LocalDate currentDate = LocalDate.now();
    private List<WeatherData> forecastData;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        this.weatherService = new WeatherApiService();
        this.airQualityService = new OpenAQService();
        this.calendar = new CalendarUtils();
        this.historyController = ControllerHolder.getHistoryController();
        setupUI();
        Platform.runLater(() -> populateCalendar(currentDate));
    }

    private void setupUI() {
        setupComboBoxes();
        setupCheckBoxes();
        setupButtons();
        setupCharts();
    }

    private void setupComboBoxes() {
        regionComboBox.getItems().addAll("Europe", "North America", "Asia");
        dateRangeComboBox.getItems().addAll("Today", "Week", "Month");
    }

    private void setupCheckBoxes() {
        showTemperatureCheck.setSelected(true);
        showHumidityCheck.setSelected(true);
    }

    private void setupButtons() {
        searchButton.setOnAction(event -> handleSearch());
        saveButton.setOnAction(event -> handleSave());
        locationField.setOnAction(event -> handleSearch());
    }

    private void setupCharts() {
        weatherChart.getXAxis().setLabel("Time");
        weatherChart.getYAxis().setLabel("Value");

        airQualityChart.getXAxis().setLabel("Pollutants");
        airQualityChart.getYAxis().setLabel("Concentration");
    }

    @FXML
    private void handleSearch() {
        String location = Optional.ofNullable(locationField.getText())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Location cannot be empty"));

        try {
            DataModel weatherDataModel = weatherService.fetchWeatherData(
                    location);
            forecastData = weatherDataModel.
                    getWeatherDataList();
            if (forecastData.isEmpty()) {
                throw new IllegalStateException("No weather data available");
            }

            for (WeatherData weatherData : forecastData) {
                System.out.println("Weather Data: " + weatherData);
                // If you need more specific details, you can print specific attributes
                System.out.println("Location: " + weatherData.getLocation());
                System.out.println("Date: " + weatherData.getFormatDate());
                System.out.println("Temperature: " + weatherData.
                        getTemperature());
                System.out.println("Humidity: " + weatherData.getHumidity());
            }

            AirQualityData airQualityData = airQualityService.
                    fetchAirQualityData(location);

            saveSearchToHistory(forecastData.get(forecastData.size() - 1));
            updateUI(weatherDataModel, airQualityData);
        } catch (Exception e) {
            AlertUtils.showAlert("Error", "Failed to fetch data: " + e.
                    getMessage());
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
    private void handleSave() {
        String location = Optional.ofNullable(locationField.getText())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Location cannot be empty"));

        SavePreferencesController preferencesController = ControllerHolder.
                getPreferencesController();
        preferencesController.addLocation(location);
    }


    private void updateUI(DataModel weatherDataModel,
            AirQualityData airQualityData) {
        Platform.runLater(() -> {
            WeatherData currentWeather = weatherDataModel.getWeatherDataList().
                    get(0);

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


            updateWeatherSummaryListView(currentWeather);
            updateAirQualityChart(airQualityData);
            updateChart(weatherDataModel);
            highlightCalendar(forecastData);
        });
    }

    private void updateWeatherSummaryListView(WeatherData currentWeather) {
        if (weatherSummaryListView == null) {
            return;
        }

        // Clear previous data in the list view
        weatherSummaryListView.getItems().clear();

        headerLabel.setText(StringUtils.capitalizeCities(
                locationField.getText()));

        // Add current weather summary to the list view
        weatherSummaryListView.getItems().add(String.format(
                "Temperature: %.1f°C",
                currentWeather.
                        getTemperature()));
        weatherSummaryListView.getItems().add(String.format(
                "Feels like: %.1f°C",
                currentWeather.
                        getFeelsLike()));
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

        // Clear existing data
        airQualityChart.getData().clear();

        // If airQualityData is null, show a "No data available" message
        if (airQualityData == null) {
            XYChart.Series<String, Number> noDataSeries = new XYChart.Series<>();
            noDataSeries.setName("Air Quality");

            // Create a single data point with "No data available"
            XYChart.Data<String, Number> noDataPoint = new XYChart.Data<>(
                    "No Data", 0);

            Text label = new Text("No data available");
            label.setStyle(
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: red;");
            label.setTranslateX(65);
            label.setTranslateY(-65);

            // Set the label to the node of the data point
            noDataPoint.setNode(label);
            noDataSeries.getData().add(noDataPoint);
            airQualityChart.getData().add(noDataSeries);

            return;
        }

        if (airQualityData != null) {
            // Create a new series for the BarChart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Air Quality");

            // Add data to the series
            if (airQualityData.getPm25() != 0.0) {
                series.getData().add(new XYChart.Data<>("PM2.5", airQualityData.
                        getPm25()));
            }
            if (airQualityData.getPm10() != 0.0) {
                series.getData().add(new XYChart.Data<>("PM10", airQualityData.
                        getPm10()));
            }
            if (airQualityData.getNo2() != 0.0) {
                series.getData().add(new XYChart.Data<>("NO2", airQualityData.
                        getNo2()));
            }

            // Add the series to the BarChart
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
            XYChart.Series<String, Number> maxTempSeries =
                    LineChartUtils.addPoints(dataModel, "max");

            XYChart.Series<String, Number> minTempSeries =
                    LineChartUtils.addPoints(dataModel, "min");

            // Add the series to the chart
            weatherChart.getData().add(maxTempSeries);
            weatherChart.getData().add(minTempSeries);

        }

        if (showHumidityCheck != null && showHumidityCheck.isSelected()) {
            XYChart.Series<String, Number> humiditySeries = LineChartUtils.
                    addPoints(dataModel, "hum");

            weatherChart.getData().add(humiditySeries);
        }
    }

    /*
     * Calendar utilities
     */
    public void populateCalendar(LocalDate selectedDate) {
        if (calendarGrid != null) {
            calendarGrid.getChildren().clear();
            YearMonth yearMonth = YearMonth.from(selectedDate);
            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

            // Adjust firstDayOfWeek
            firstDayOfWeek = (firstDayOfWeek == 7) ? 6 : firstDayOfWeek - 1;

            // Add the weekday labels (Mon, Tue, Wed, etc.) in the first row
            String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
                "Sun"};
            for (int col = 0; col < 7; col++) {
                Label weekdayLabel = new Label(daysOfWeek[col]);
                weekdayLabel.setMinSize(40, 40);
                weekdayLabel.setStyle(
                        "-fx-alignment: center; -fx-font-weight: bold;");
                calendarGrid.add(weekdayLabel, col, 0);  // Place at the top row (row = 0)
            }

            int rows = 6, cols = 7, dayCounter = 1;
            for (int row = 1; row < rows; row++) {  // Start adding days from row 1 (below the weekday labels)
                for (int col = 0; col < cols; col++) {
                    // Create a StackPane for each calendar cell
                    StackPane stackPane = new StackPane();
                    stackPane.setMinSize(40, 40);  // Set the size of the cell
                    stackPane.setStyle(
                            "-fx-border-color: lightgray; -fx-alignment: center;");

                    // Create the label for the day number
                    Label dayLabel = new Label();
                    dayLabel.setMinSize(40, 40);
                    dayLabel.setStyle("-fx-alignment: center;"); // Ensuring text is centered

                    // Calculate the correct cell for the first day placement
                    if (dayCounter <= yearMonth.lengthOfMonth()) {
                        // If we're in the first week and past the starting day or in subsequent weeks
                        if (row == 1 && col >= firstDayOfWeek || row > 1) {
                            dayLabel.setText(String.valueOf(dayCounter));
                            LocalDate cellDate = firstDayOfMonth.plusDays(
                                    dayCounter - 1);
                            dayLabel.setUserData(cellDate);  // Store the date in the label's user data
                            dayCounter++;
                        }
                    }

                    // Add the dayLabel to the StackPane
                    stackPane.getChildren().add(dayLabel);

                    // Add the StackPane to the grid
                    calendarGrid.add(stackPane, col, row);
                }
            }
        }
    }

    @FXML
    private void handlePreviousMonth() {
        currentDate = currentDate.minusMonths(1);  // Move to the previous month
        populateCalendar(currentDate);
        if (forecastData != null) {
            highlightCalendar(forecastData);  // Highlight forecast data for the new month
        }
    }

    @FXML
    private void handleNextMonth() {
        currentDate = currentDate.plusMonths(1);  // Move to the next month
        populateCalendar(currentDate);
        if (forecastData != null) {
            highlightCalendar(forecastData);  // Highlight forecast data for the new month
        }
    }

    public void highlightCalendar(List<WeatherData> forecastData) {
        Map<LocalDate, String> highlightMap = new HashMap<>();

        // Find the warmest day without rain
        WeatherData bestDay = forecastData.stream()
                .filter(data -> data.getPrecipitation() < 1) // No rain
                .max(Comparator.comparingDouble(WeatherData::getTemperature)) // Max temperature
                .orElse(null);

        // Find the coldest day(s) or days with rain
        WeatherData worstDay;

        List<WeatherData> rainyDays = forecastData.stream()
                .filter(data -> data.getPrecipitation() >= 1) // Days with rain
                .collect(Collectors.toList());

        if (!rainyDays.isEmpty()) {
            // If there are rainy days, find the one with the most precipitation
            worstDay = rainyDays.stream()
                    .max(Comparator.comparingDouble(
                            WeatherData::getPrecipitation)) // Max precipitation
                    .orElse(null);
        } else {
            // If no rainy days, find the coldest day
            worstDay = forecastData.stream()
                    .min(Comparator.comparingDouble(WeatherData::getTemperature)) // Min temperature
                    .
                    orElse(null);
        }

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
    }


    public void updateCalendarUI(Map<LocalDate, String> highlightMap) {
        if (calendarGrid != null) {
            System.out.println("CalendarGrid isn't null");
            for (Node node : calendarGrid.getChildren()) {
                if (node instanceof StackPane stackPane) {
                    Label dayLabel = (Label) stackPane.getChildren().get(0);  // Get the Label from StackPane
                    String text = dayLabel.getText();
                    if (!text.isEmpty()) {
                        LocalDate cellDate = getDateFromCell(dayLabel);

                        // Remove previous dots
                        stackPane.getChildren().removeIf(child ->
                                child instanceof Circle);

                        if (cellDate != null && highlightMap.containsKey(
                                cellDate)) {
                            String styleClass = highlightMap.get(cellDate);
                            dayLabel.getStyleClass().add(styleClass);

                            // Add a colored dot based on the weather type
                            Circle dot = new Circle(15);
                            dot.setTranslateX(0);
                            dot.setTranslateY(0);
                            dot.setTranslateZ(0);
                            dot.setStyle("-fx-alignment: center;");
                            dot.setOpacity(0.5);

                            switch (styleClass) {
                                case "best-weather":
                                    dot.setFill(Color.GREEN); // Green dot for best weather
                                    break;
                                case "worst-weather":
                                    dot.setFill(Color.RED);  // Red dot for worst weather
                                    break;
                                case "moderate-weather":
                                    dot.setFill(Color.YELLOW); // Yellow dot for moderate weather
                                    break;
                                default:
                                    dot.setFill(Color.TRANSPARENT);  // No dot for default
                                    break;
                            }

                            // Add the dot to the StackPane
                            stackPane.getChildren().add(dot);
                        }
                    }
                }
            }
        } else {
            System.out.println("CalendarGrid is null");
        }
    }

    private LocalDate getDateFromCell(Label cell) {
        Object userData = cell.getUserData();
        return userData instanceof LocalDate ? (LocalDate) userData : null;
    }
}
