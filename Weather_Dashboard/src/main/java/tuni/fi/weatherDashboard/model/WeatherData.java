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
    private double feelsLike;
    private Double minTemp;

    public WeatherData(String location, String dateTime, double temperature,
            double humidity, double precipitation, double feelsLike,
            Double minTemp) {
        this.location = location;
        this.dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(Long.parseLong(dateTime)),
            ZoneId.systemDefault()
        );
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.feelsLike = feelsLike;
        this.minTemp = minTemp;
    }

    public WeatherData(String location, String dateTime, double temperature,
            double humidity, double precipitation, double feelsLike) {
        this(location, dateTime, temperature, humidity, precipitation, feelsLike,
                null);
    }

    // Getters
    public LocalDateTime getDateTime() { return dateTime; }
    public double getTemperature() {
        return temperature;
    }

    public double getMinTemp() {
        return minTemp;
    }
    public double getHumidity() { return humidity; }
    public double getPrecipitation() {
        return precipitation;
    }

    public double getFeelsLike() {
        return feelsLike;
    }
    public String getLocation() { return location; }
    public String getDescription() {
        return description;
    }

    public LocalDateTime getLocalDate() {
        return this.dateTime;
    }

    public boolean isRain(String description) {
        return description.toLowerCase().contains("rain");
    }

    // Setters
    public void setLocation(String location) { this.location = location; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("Weather at %s: %.1f°C, %.1f%% humidity, %.1fmm precipitation",
                location, temperature, humidity, precipitation);
    }
}