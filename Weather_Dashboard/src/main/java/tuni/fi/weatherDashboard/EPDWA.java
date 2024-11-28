package tuni.fi.weatherDashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tuni.fi.weatherDashboard.controller.AboutController;
import tuni.fi.weatherDashboard.controller.ControllerHolder;
import tuni.fi.weatherDashboard.controller.DashboardController;
import tuni.fi.weatherDashboard.controller.HistoryController;
import tuni.fi.weatherDashboard.controller.SavePreferencesController;

import java.util.HashMap;
import java.util.Map;

public class EPDWA extends Application {

    // Static field to hold the EPDWA instance
    private static EPDWA instance;

    // Map to store loaded scenes
    private final Map<String, Scene> loadedScenes = new HashMap<>();

    public static EPDWA getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            instance = this; // Store the instance

            // Create controller instances
            HistoryController historyController = new HistoryController();
            DashboardController dashboardController = new DashboardController();
            SavePreferencesController preferencesController =
                    new SavePreferencesController();
            AboutController aboutController = new AboutController();

            // Store controllers in ControllerHolder
            ControllerHolder.setDashboardController(dashboardController);
            ControllerHolder.setHistoryController(historyController);
            ControllerHolder.setPreferencesController(preferencesController);
            ControllerHolder.setAboutController(aboutController);

            // Create and load History FXML
            FXMLLoader historyLoader = new FXMLLoader(getClass().getResource(
                    "/tuni/fi/weatherDashboard/view/History.fxml"));
            historyLoader.setController(historyController);  // Set controller manually
            Scene history = new Scene(historyLoader.load(), 800, 600);
            loadedScenes.put("History.fxml", history);  // Add scene to the map

            // Create and load Dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/tuni/fi/weatherDashboard/view/Dashboard.fxml"));
            loader.setController(dashboardController);  // Set controller manually
            Scene dash = new Scene(loader.load(), 800, 600);
            loadedScenes.put("Dashboard.fxml", dash);  // Add scene to the map

            // Create and load Save Preferences FXML
            FXMLLoader preferencesLoader = new FXMLLoader(getClass().
                    getResource(
                            "/tuni/fi/weatherDashboard/view/SavePreferences.fxml"));
            preferencesLoader.setController(preferencesController);  // Set controller manually
            Scene preferences = new Scene(preferencesLoader.load(), 800, 600);
            loadedScenes.put("SavePreferences.fxml", preferences);  // Add scene to the map

            // Create and load About FXML
            FXMLLoader aboutLoader = new FXMLLoader(getClass().getResource(
                    "/tuni/fi/weatherDashboard/view/About.fxml"));
            aboutLoader.setController(aboutController);  // Set controller manually
            Scene about = new Scene(aboutLoader.load(), 800, 600);
            loadedScenes.put("About.fxml", about);  // Add scene to the map


            // debug
            System.out.println("Loaded scenes map contents:");
            for (Map.Entry<String, Scene> entry : getLoadedScenes().
                    entrySet()) {
                System.out.println("Scene: " + entry.getKey() + " - " + entry.
                        getValue());
            }

            // Continue with setting the scene and showing the stage
            primaryStage.setTitle("Event Planner's Weather Dashboard");
            primaryStage.setScene(dash);  // Default to Dashboard scene
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter for loadedScenes map
    public Map<String, Scene> getLoadedScenes() {
        return loadedScenes;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
