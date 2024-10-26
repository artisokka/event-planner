package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tuni.fi.weatherDashboard.model.WeatherData;
import java.time.LocalDateTime;

import javafx.fxml.FXML;

public class HistoryController extends BaseController {
    @FXML private TableView<WeatherData> historyTable;
    @FXML private TableColumn<WeatherData, String> cityColumn;
    @FXML private TableColumn<WeatherData, LocalDateTime> dateColumn;
    @FXML private TableColumn<WeatherData, Double> temperatureColumn;
    @FXML private ListView<String> eventsList;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        setupTable();
        setupEventsList();
    }

    private void setupTable() {
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
    }

    private void setupEventsList() {
        eventsList.getItems().addAll(
            "Sample Event 1",
            "Sample Event 2",
            "Sample Event 3"
        );
    }
}
