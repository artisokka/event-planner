package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class BaseController {
    @FXML protected Button homeButton;
    @FXML protected Button historyButton;
    @FXML protected Button savedPreferencesButton;
    @FXML protected Button aboutButton;

    @FXML
    protected void initialize() {
        setupNavigation();
    }

    private void setupNavigation() {
        homeButton.setOnAction(e -> navigateTo("Dashboard.fxml"));
        historyButton.setOnAction(e -> navigateTo("History.fxml"));
        savedPreferencesButton.setOnAction(e -> navigateTo("SavePreferences.fxml"));
        aboutButton.setOnAction(e -> navigateTo("About.fxml"));
    }

    protected void navigateTo(String fxml) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tuni/fi/weatherDashboard/view/" + fxml));
        Parent root = loader.load();
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    } catch (IOException e) {
        System.err.println("Failed to load FXML file: " + fxml);
    } catch (NullPointerException e) {
        System.err.println("FXML file not found or button not initialized properly: " + fxml);
    }
}
}

// tuni.fi.weatherDashboard.controller/HistoryController.java


// tuni.fi.weatherDashboard.controller/SavePreferencesController.java


// tuni.fi.weatherDashboard.controller/AboutController.java
