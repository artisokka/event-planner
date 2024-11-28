package tuni.fi.weatherDashboard.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author janik
 */
public class AlertUtils {
    public static void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert;
            // Check the title to determine the alert type
            switch (title) {
                case "Error":
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(title);
                    alert.setContentText(content);
                    break;
                case "Success":
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(title);
                    alert.setContentText(content);
                    break;
                case "Info":
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(title);
                    alert.setContentText(content);
                    break;
                default:
                    // Default to information type if title is not recognized
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setContentText(content);
                    break;
            }
            alert.showAndWait();
        });
    }
}
