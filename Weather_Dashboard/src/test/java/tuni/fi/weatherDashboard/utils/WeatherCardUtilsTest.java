package tuni.fi.weatherDashboard.utils;

/**
 *
 * @author afeera
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.layout.VBox;
import tuni.fi.weatherDashboard.model.WeatherData;
import javafx.application.Platform;

class WeatherCardUtilsTest extends TestBase {
    private WeatherData testWeatherData;

    @BeforeEach
    void setUp() {
        testWeatherData = new WeatherData("Helsinki", "1701432000", 20.0, 65.0, 0.0, 19.0);
        testWeatherData.setDescription("Sunny");
    }

    @Test
    void createWeatherCard_ValidData_CreatesCard() {
        Platform.runLater(() -> {
            VBox card = WeatherCardUtils.createWeatherCard(testWeatherData);
            assertNotNull(card);
            assertTrue(card.getChildren().size() > 0);
            assertEquals("-fx-background-color: #D0E7F9", 
                card.getStyle().split(";")[0].trim());
        });
    }
}