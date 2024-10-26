package com.eventplanner.dashboard.controller;

import com.eventplanner.dashboard.model.DataModel;
import com.eventplanner.dashboard.service.WeatherApiService;
import javafx.scene.control.Label;

public class DashboardController {

    private final WeatherApiService weatherApiService;
    private final Label weatherLabel;

    // Constructor takes a Label parameter
    public DashboardController(Label weatherLabel) {
        this.weatherLabel = weatherLabel;
        this.weatherApiService = new WeatherApiService();
    }

    public void fetchWeatherData(String cityName) {
        DataModel dataModel = weatherApiService.fetchWeatherData(cityName);
        if (dataModel != null) {
            weatherLabel.setText(dataModel.toString());
        } else {
            weatherLabel.setText("No data available.");
        }
    }
}
