package com.eventplanner.dashboard.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class WeatherData {

    private LocalDateTime dateTime;
    private double temperature;
    private double humidity;
    private double precipitation;

    public WeatherData(String dateTime, double temperature, double humidity,
            double precipitation) {
        this.dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.
                parseLong(dateTime)), ZoneId.systemDefault());
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPrecipitation() {
        return precipitation;
    }

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
