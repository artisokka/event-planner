package tuni.fi.weatherDashboard.utils;

/**
 *
 * @author afeera
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;

class AlertUtilsTest extends TestBase {

    @Test
    void showAlert_ErrorTitle_DoesNotThrowException() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> 
                AlertUtils.showAlert("Error", "Test error message")
            );
        });
    }

    @Test
    void showAlert_InfoTitle_DoesNotThrowException() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> 
                AlertUtils.showAlert("Info", "Test info message")
            );
        });
    }

    @Test
    void showAlert_SuccessTitle_DoesNotThrowException() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> 
                AlertUtils.showAlert("Success", "Test success message")
            );
        });
    }

    @Test
    void showAlert_UnknownTitle_DoesNotThrowException() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> 
                AlertUtils.showAlert("Unknown", "Test message")
            );
        });
    }
}