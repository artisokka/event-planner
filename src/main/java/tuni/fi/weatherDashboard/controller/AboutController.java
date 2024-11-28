package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javafx.fxml.FXML;

public class AboutController extends BaseController {
    @FXML private TextArea aboutText;

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        setupAboutText();
    }

    private void setupAboutText() {
        aboutText.setText("""
            Event Planner's Weather Dashboard
            Version 1.0
            
            This application helps event planners make informed decisions about 
            outdoor events by providing:
            
            • Weather forecasts
            • Air quality data
            • Historical weather information
            • Location-based planning tools
            
            Created as part of the Software Design course project.
            """);
        aboutText.setEditable(false);
        aboutText.setWrapText(true);
    }
}

