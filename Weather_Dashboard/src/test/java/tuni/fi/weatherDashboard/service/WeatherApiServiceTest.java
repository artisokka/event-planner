package tuni.fi.weatherDashboard.service;

/**
 *
 * @author afeera
 */

import java.util.ArrayList;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;

class WeatherApiServiceTest {
    private WeatherApiService weatherService;
    private static final String TEST_CITY = "Helsinki";

    @BeforeEach
    void setUp() {
        weatherService = new WeatherApiService();
    }

    @Test
    void fetchWeatherData_ValidCity_ReturnsDataModel() {
        // Act
        DataModel result = weatherService.fetchWeatherData(TEST_CITY);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getWeatherDataList());
        assertFalse(result.getWeatherDataList().isEmpty());
    }

    @Test
    void fetchWeatherData_EmptyCity_ThrowsException() {
        // Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            weatherService.fetchWeatherData("");
        });
        assertEquals("Please enter a city name", exception.getMessage());
    }

    @Test
    void fetchWeatherData_NullCity_ThrowsException() {
        // Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            weatherService.fetchWeatherData(null);
        });
        assertEquals("Please enter a city name", exception.getMessage());
    }

    @Test
    void fetchWeatherData_ValidCity_DataIsOrdered() {
        // Act
        DataModel result = weatherService.fetchWeatherData(TEST_CITY);
        
        // Assert
        ArrayList<WeatherData> weatherDataList = result.getWeatherDataList();
        for (int i = 1; i < weatherDataList.size(); i++) {
            assertTrue(weatherDataList.get(i-1).getDateTime()
                .compareTo(weatherDataList.get(i).getDateTime()) <= 0,
                "Weather data should be ordered by date/time");
        }
    }
}
