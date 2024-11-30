package tuni.fi.weatherDashboard.model;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;

public class WeatherData {
    private LocalDateTime dateTime;
    private double temperature;
    private double humidity;
    private double precipitation;
    private String location;
    private String description;

    public WeatherData(String dateTime, double temperature, double humidity, 
                      double precipitation) {
        this.dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(Long.parseLong(dateTime)), 
            ZoneId.systemDefault()
        );
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
    }

    // Getters
    public LocalDateTime getDateTime() { return dateTime; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getPrecipitation() { return precipitation; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }

    // Setters
    public void setLocation(String location) { this.location = location; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("Weather at %s: %.1f°C, %.1f%% humidity, %.1fmm precipitation",
                location, temperature, humidity, precipitation);
    }
}