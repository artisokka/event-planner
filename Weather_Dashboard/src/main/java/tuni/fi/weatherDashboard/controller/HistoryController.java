package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tuni.fi.weatherDashboard.model.WeatherData;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

public class HistoryController extends BaseController {

    @FXML private TableView<WeatherData> historyTable;
    @FXML private TableColumn<WeatherData, String> cityColumn;
    @FXML private TableColumn<WeatherData, LocalDateTime> dateColumn;
    @FXML private TableColumn<WeatherData, Double> temperatureColumn;
    @FXML private ListView<String> eventsList;

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
    }

    private void setupTable() {
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>(
                "temperature"));

        //addTestData();
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
        WeatherData testData = new WeatherData("Testi", "1732714923",
                10.0, 15.0,
                5.0);

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


    // Some events for show, no functionality whatsoever
    private void setupEventsList() {
        eventsList.getItems().addAll(
                "Rosa's Birthday",
                "Emmett's Wedding",
                "Independence Day"
        );
    }
}
