package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tuni.fi.weatherDashboard.model.WeatherData;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import tuni.fi.weatherDashboard.utils.DateTimeUtils;

public class HistoryController extends BaseController {

    @FXML private TableView<WeatherData> historyTable;
    @FXML private TableColumn<WeatherData, String> cityColumn;
    @FXML private TableColumn<WeatherData, LocalDateTime> dateColumn;
    @FXML private TableColumn<WeatherData, Double> temperatureColumn;
    @FXML private ListView<String> eventsList;
    @FXML private LineChart<String, Number> historyChart; // Reference to the chart

    private final ObservableList<WeatherData> searchHistory = FXCollections.
            observableArrayList();
    private final ObservableList<WeatherData> cachedHistory = FXCollections.
            observableArrayList();

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        setupTable();

        if (historyTable != null) {
            historyTable.setItems(searchHistory);
            System.out.println("History table re-initialized with data.");
        }

        setupEventsList();
        loadCachedEntries();

        // Add listener for row selection
        historyTable.getSelectionModel().selectedItemProperty().addListener((
                observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateHistoryChart(newValue); // Update chart with the selected entry's data
            }
        });
    }

    private void setupTable() {
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>(
                "temperature"));
    }

    public void loadCachedEntries() {
        if (!cachedHistory.isEmpty()) {
            searchHistory.addAll(cachedHistory);
            cachedHistory.clear();
            if (historyTable != null) {
                historyTable.refresh();
            }
            System.out.println("Loaded cached history entries.");
        }
    }

    public void addTestData() {
        WeatherData testData =
                new WeatherData("Testi", "1732714923", 10.0, 15.0, 5.0);
        addSearchToHistory(testData);
    }

    public void addSearchToHistory(WeatherData weatherData) {
        if (weatherData != null) {
            if (historyTable != null) {
                // Insert the new entry at the beginning of the list (index 0)
                searchHistory.add(0, weatherData);
                historyTable.refresh(); // Refresh to reflect the updated order
                System.out.println("Added search to history table.");
            } else {
                cachedHistory.add(weatherData); // Cache until the table is initialized
                System.out.println(
                        "Cached history entry; table not initialized.");
            }
        }
    }

    private void setupEventsList() {
        eventsList.getItems().addAll(
                "Rosa's Birthday",
                "Emmett's Wedding",
                "Independence Day"
        );
    }

    // Method to update the chart with the selected weather data
    private void updateHistoryChart(WeatherData selectedData) {
        if (historyChart == null) {
            System.err.println("History chart is null!");
            return;
        }

        // Clear existing data
        historyChart.getData().clear();

        // Create the series for data points
        XYChart.Series<String, Number> tempSeries = new XYChart.Series<>();
        tempSeries.setName("Temperature °C");

        XYChart.Series<String, Number> humiditySeries = new XYChart.Series<>();
        humiditySeries.setName("Humidity %");

        // Format the date
        String formattedDate = DateTimeUtils.formatDateTime(selectedData.
                getDateTime());

        // Create and add temperature data point
        XYChart.Data<String, Number> tempDataPoint = new XYChart.Data<>(
                formattedDate, selectedData.getTemperature());
        Text tempLabel = new Text(String.format("%d°C", (int) selectedData.
                getTemperature()));
        tempLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        tempLabel.setTranslateX(10);
        tempLabel.setTranslateY(-15);
        tempDataPoint.setNode(tempLabel);
        tempSeries.getData().add(tempDataPoint);

        // Create and add humidity data point
        XYChart.Data<String, Number> humidityDataPoint = new XYChart.Data<>(
                formattedDate, selectedData.getHumidity());
        Text humidityLabel = new Text(String.format("%d%%", (int) selectedData.
                getHumidity()));
        humidityLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        humidityLabel.setTranslateX(10);
        humidityLabel.setTranslateY(15);
        humidityDataPoint.setNode(humidityLabel);
        humiditySeries.getData().add(humidityDataPoint);

        // Add both series to the chart
        historyChart.getData().addAll(tempSeries, humiditySeries);
    }
}
