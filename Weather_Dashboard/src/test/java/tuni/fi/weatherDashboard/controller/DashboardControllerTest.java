
package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;
import java.time.LocalDate;
import java.util.ArrayList;
import tuni.fi.weatherDashboard.model.*;

class DashboardControllerTest {
    private DashboardController controller;
    private static boolean jfxIsSetup = false;

    

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            controller = new DashboardController();
            // Initialize required JavaFX components
            controller.initialize();
        });
    }
    

    @Test
    void updateMonthName_ValidDate_SetsCorrectName() {
        Platform.runLater(() -> {
            // Arrange
            LocalDate testDate = LocalDate.of(2023, 12, 1);
            
            // Act
            controller.updateMonthName(testDate);
            
            // Assert
            assertEquals("December", controller.monthName.getText());
        });
    }

    @Test
    void highlightCalendar_ValidData_UpdatesCalendar() {
        Platform.runLater(() -> {
            // Arrange
            ArrayList<WeatherData> testData = new ArrayList<>();
            testData.add(new WeatherData("Helsinki", "1701432000", 20.0, 65.0, 0.0, 19.0));
            
            // Act
            controller.highlightCalendar(testData);
            
            // Assert - verify calendar is updated
            assertNotNull(controller.calendarGrid);
            assertFalse(controller.calendarGrid.getChildren().isEmpty());
        });
    }
}
