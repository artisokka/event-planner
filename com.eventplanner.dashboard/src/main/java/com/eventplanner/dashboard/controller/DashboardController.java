package com.eventplanner.dashboard.controller;

/**
 *
 * @author afeera
 */

import com.eventplanner.dashboard.model.DataModel;
import com.eventplanner.dashboard.model.WeatherData;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DashboardController {
    private final DataModel model;

    public DashboardController() {
        this.model = new DataModel();
    }

    public List<WeatherData> fetchWeatherData(String location, LocalDate startDate, LocalDate endDate) {
        try {
            model.fetchWeatherData(location, startDate, endDate);
            return model.getWeatherDataList();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Handle the error appropriately, maybe set an error state or throw a custom exception
            return null;
        }
    }

    // Add more methods as needed for other functionalities
}
