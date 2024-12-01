package tuni.fi.weatherDashboard.utils;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.chart.XYChart;
import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;
import java.util.ArrayList;

class LineChartUtilsTest {
    private DataModel testDataModel;

    @BeforeEach
    void setUp() {
        ArrayList<WeatherData> testData = new ArrayList<>();
        // Create WeatherData with minTemp in constructor
        WeatherData data = new WeatherData(
            "Helsinki",          // location
            "1701432000",       // dateTime
            20.0,               // temperature
            65.0,               // humidity
            0.0,                // precipitation
            19.0,               // feelsLike
            15.0                // minTemp
        );
        testData.add(data);
        testDataModel = new DataModel(testData);
    }

    @Test
    void addPoints_Humidity_ReturnsCorrectSeries() {
        XYChart.Series<String, Number> series = LineChartUtils.addPoints(testDataModel, "hum");
        assertNotNull(series);
        assertEquals("Humidity %", series.getName());
        assertFalse(series.getData().isEmpty());
        assertEquals(65.0, series.getData().get(0).getYValue().doubleValue(), 0.01);
    }

    @Test
    void addPoints_MinTemperature_ReturnsCorrectSeries() {
        XYChart.Series<String, Number> series = LineChartUtils.addPoints(testDataModel, "min");
        assertNotNull(series);
        assertEquals("Min Temperature °C", series.getName());
        assertFalse(series.getData().isEmpty());
        assertEquals(15.0, series.getData().get(0).getYValue().doubleValue(), 0.01);
    }

    @Test
    void addPoints_MaxTemperature_ReturnsCorrectSeries() {
        XYChart.Series<String, Number> series = LineChartUtils.addPoints(testDataModel, "max");
        assertNotNull(series);
        assertEquals("Max Temperature °C", series.getName());
        assertFalse(series.getData().isEmpty());
        assertEquals(20.0, series.getData().get(0).getYValue().doubleValue(), 0.01);
    }
}