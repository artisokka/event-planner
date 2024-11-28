package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;

public class SavePreferencesController extends BaseController {
    @FXML private GridPane preferencesGrid;
    @FXML private ListView<String> savedLocationsList;
    private Preferences userPreferences;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        userPreferences = Preferences.userNodeForPackage(this.getClass());
        loadSavedLocations();
    }

    private void loadSavedLocations() {
        String savedLocs = userPreferences.get("savedLocations", "");
        if (!savedLocs.isEmpty()) {
            String[] locations = savedLocs.split(",");
            savedLocationsList.getItems().addAll(locations);
        }
    }
}
