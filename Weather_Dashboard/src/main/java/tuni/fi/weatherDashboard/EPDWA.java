package com.eventplanner.dashboard;

import com.eventplanner.dashboard.controller.DashboardController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EPDWA extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Event Planner Dashboard");

        // Create the search box for location
        TextField locationField = new TextField();
        locationField.setPromptText("Enter location...");

        // Create the fetch weather button
        Button fetchWeatherButton = new Button("Fetch Weather");

        // Create a label to display the weather data
        Label weatherLabel = new Label();

        // Create a DashboardController instance
        DashboardController dashboardController = new DashboardController(
                weatherLabel);

        // Set up button action
        fetchWeatherButton.setOnAction(event -> {
            String location = locationField.getText();
            dashboardController.fetchWeatherData(location);
        });

        // Set up the front layout
        VBox vbox = new VBox(locationField, fetchWeatherButton, weatherLabel);
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
