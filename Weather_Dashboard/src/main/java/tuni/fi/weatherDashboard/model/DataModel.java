package tuni.fi.weatherDashboard.model;

import java.util.ArrayList;

public class DataModel {
    private ArrayList<WeatherData> weatherDataList;

    public DataModel(ArrayList<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    public ArrayList<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (WeatherData data : weatherDataList) {
            result.append(data.toString()).append("\n");
        }
        return result.toString();
    }
}