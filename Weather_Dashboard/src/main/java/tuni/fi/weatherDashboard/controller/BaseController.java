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
import tuni.fi.weatherDashboard.EPDWA;

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
        savedPreferencesButton.setOnAction(e -> navigateTo(
                "SavePreferences.fxml"));
        aboutButton.setOnAction(e -> navigateTo("About.fxml"));
    }

    protected void navigateTo(String fxml) {
        try {
            Stage stage = (Stage) homeButton.getScene().getWindow();

            // Access EPDWA instance and check if the scene is already loaded
            EPDWA epdwa = EPDWA.getInstance();

            // If the scene is already loaded, use the existing scene
            if (epdwa.getLoadedScenes().containsKey(fxml)) {
                Scene existingScene = epdwa.getLoadedScenes().get(fxml);
                stage.setScene(existingScene);  // Switch to the existing scene
                stage.getScene().getRoot().requestLayout(); // Refresh page
            } else {
                // If not loaded, load the FXML and create a new Scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/tuni/fi/weatherDashboard/view/" + fxml));
                Parent root = loader.load(); // Load a fresh instance of the FXML

                // Store the new scene in the map
                Scene newScene = new Scene(root);
                epdwa.getLoadedScenes().put(fxml, newScene);  // Add to the map

                // Set the scene on the stage
                stage.setScene(newScene);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
