package com.eventplanner.dashboard.model;

import com.eventplanner.dashboard.service.WeatherApiService;
/**
 *
 * @author afeera
 */

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DataModel {
    private List<WeatherData> weatherDataList;
    private final WeatherApiService weatherApiService;

    public DataModel() {
        this.weatherApiService = new WeatherApiService();
    }

    public void fetchWeatherData(String location, LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {
        weatherDataList = weatherApiService.fetchWeatherData(location, startDate, endDate);
    }

    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }
}
