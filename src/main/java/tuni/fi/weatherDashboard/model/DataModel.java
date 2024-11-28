package tuni.fi.weatherDashboard.model;

import java.util.List;

public class DataModel {
    private List<WeatherData> weatherDataList;

    public DataModel(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    public List<WeatherData> getWeatherDataList() {
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