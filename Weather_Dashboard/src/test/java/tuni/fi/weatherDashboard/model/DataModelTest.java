
package tuni.fi.weatherDashboard.model;

/**
 *
 * @author afeera
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class DataModelTest {
    private DataModel dataModel;
    private ArrayList<WeatherData> testData;

    @BeforeEach
    void setUp() {
        testData = new ArrayList<>();
        testData.add(new WeatherData("Helsinki", "1701432000", 20.0, 65.0, 0.0, 19.0));
        testData.add(new WeatherData("Helsinki", "1701435600", 22.0, 70.0, 0.0, 21.0));
        dataModel = new DataModel(testData);
    }

    @Test
    void constructor_ValidData_CreatesModel() {
        // Assert
        assertNotNull(dataModel);
        assertEquals(testData, dataModel.getWeatherDataList());
    }

    @Test
    void getWeatherDataList_ReturnsCorrectData() {
        // Act
        ArrayList<WeatherData> result = dataModel.getWeatherDataList();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(20.0, result.get(0).getTemperature());
        assertEquals(22.0, result.get(1).getTemperature());
    }

    @Test
    void toString_ReturnsFormattedString() {
        // Act
        String result = dataModel.toString();
        
        // Assert
        assertTrue(result.contains("Helsinki"));
        assertTrue(result.contains("20.0"));
        assertTrue(result.contains("22.0"));
    }
}
