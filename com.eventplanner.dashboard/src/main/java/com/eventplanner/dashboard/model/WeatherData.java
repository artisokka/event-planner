package com.eventplanner.dashboard.model;

/**
 *
 * @author afeera
 */

import java.time.LocalDateTime;

public class WeatherData {
    private LocalDateTime dateTime;
    private double temperature;
    private double humidity;
    private double precipitation;

    public WeatherData(LocalDateTime dateTime, double temperature, double humidity, double precipitation) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
    }

    // Getters
    public LocalDateTime getDateTime() { return dateTime; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getPrecipitation() { return precipitation; }

    @Override
    public String toString() {
        return "WeatherData{" +
                "dateTime=" + dateTime +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipitation=" + precipitation +
                '}';
    }
}
