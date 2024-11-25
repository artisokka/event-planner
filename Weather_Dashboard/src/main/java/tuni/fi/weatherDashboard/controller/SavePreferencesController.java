package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import java.util.prefs.Preferences;

public class SavePreferencesController extends BaseController {
    @FXML private GridPane preferencesGrid;
    @FXML private ListView<String> savedLocationsList;
    @FXML private Button removeButton;
    @FXML private Button clearAllButton;

    private Preferences userPreferences;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        userPreferences = Preferences.userNodeForPackage(this.getClass());
        loadSavedLocations();

        if (removeButton != null) {
            removeButton.setOnAction(event -> handleRemoveSelectedLocation());
        }

        if (clearAllButton != null) {
            clearAllButton.setOnAction(event -> handleClearAllLocations());
        }
    }

    private void loadSavedLocations() {
        String savedLocs = userPreferences.get("savedLocations", "");
        if (!savedLocs.isEmpty()) {
            String[] locations = savedLocs.split(",");
            savedLocationsList.getItems().addAll(locations);
        }
    }

    /**
     * Save the current list of locations back to preferences.
     */
    private void saveLocationsToPreferences() {
        StringBuilder locationsBuilder = new StringBuilder();
        for (String location : savedLocationsList.getItems()) {
            if (locationsBuilder.length() > 0) {
                locationsBuilder.append(",");
            }
            locationsBuilder.append(location);
        }
        userPreferences.put("savedLocations", locationsBuilder.toString());
    }

    /**
     * Handle the removal of the selected location from the ListView.
     */
    private void handleRemoveSelectedLocation() {
        String selectedLocation = savedLocationsList.getSelectionModel().
                getSelectedItem();
        if (selectedLocation != null) {
            savedLocationsList.getItems().remove(selectedLocation);
            saveLocationsToPreferences(); // Update preferences after removal
        }
    }

    /**
     * Handle clearing all saved locations from the ListView and preferences.
     */
    private void handleClearAllLocations() {
        savedLocationsList.getItems().clear();
        userPreferences.remove("savedLocations"); // Clear preferences
    }
}
