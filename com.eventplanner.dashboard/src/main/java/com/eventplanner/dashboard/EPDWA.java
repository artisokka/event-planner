package com.eventplanner.dashboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main application class for running Event Planner Dashboard.
 *
 * @author janika
 */
public class EPDWA extends Application {

    public static void main(String[] args) {
        launch(args);  // Launches the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Event Planner Dashboard");  // Set the title of the window

        // Create a simple button
        Button btn = new Button("Click Me!");
        btn.setOnAction(event -> System.out.println("Button was clicked!"));

        // Set up the layout
        StackPane root = new StackPane();
        root.getChildren().add(btn);  // Add button to the layout

        // Create a scene and set it in the primary stage
        Scene scene = new Scene(root, 300, 250);  // Width: 300, Height: 250
        primaryStage.setScene(scene);

        primaryStage.show();  // Show the primary stage
    }
}
