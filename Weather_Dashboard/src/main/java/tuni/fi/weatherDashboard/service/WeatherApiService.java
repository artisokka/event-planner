package tuni.fi.weatherDashboard.service;

import tuni.fi.weatherDashboard.model.DataModel;
import tuni.fi.weatherDashboard.model.WeatherData;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;

public class WeatherApiService {

    private static final String FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast";
    private final String API_KEY = "b7342cc2f1ed92e5f2a6e98263b04190";

    public DataModel fetchWeatherData(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a city name");
        }

        List<WeatherData> weatherDataList = new ArrayList<>();
        try {
            // Clean and encode the city name
            String encodedCity = URLEncoder.encode(cityName.trim(),
                    StandardCharsets.UTF_8.toString());
            String apiUrl = String.format("%s?q=%s&units=metric&appid=%s",
                    FORECAST_URL, encodedCity, API_KEY);

            System.out.println("Fetching 5-day weather forecast for: " +
                    cityName);
            String jsonResponse = sendGetRequest(apiUrl);

            // System.out.println("Raw API Response: " + jsonResponse);

            weatherDataList = parse5DayForecast(jsonResponse);

            System.out.println("Successfully retrieved weather forecast for: " +
                    cityName);
        } catch (Exception e) {
            System.err.println("Error fetching weather data for " + cityName +
                    ": " + e.getMessage());
            e.printStackTrace();
        }

        return new DataModel(weatherDataList);
    }

    private List<WeatherData> parse5DayForecast(String jsonResponse) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray listArray = jsonObject.getJSONArray("list");

            // Aggregation logic for daily data
            Map<String, List<WeatherData>> dailyDataMap = new HashMap<>();

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject forecast = listArray.getJSONObject(i);

                long dt = forecast.getLong("dt"); // Unix timestamp
                JSONObject main = forecast.getJSONObject("main");
                double temperature = main.getDouble("temp");
                double humidity = main.getDouble("humidity");

                double precipitation = 0.0;
                if (forecast.has("rain")) {
                    precipitation = forecast.getJSONObject("rain").optDouble(
                            "3h", 0.0);
                } else if (forecast.has("snow")) {
                    precipitation = forecast.getJSONObject("snow").optDouble(
                            "3h", 0.0);
                }

                String description = forecast.getJSONArray("weather").
                        getJSONObject(0).getString("description");

                WeatherData weatherData = new WeatherData(
                        String.valueOf(dt), // Pass the Unix timestamp as a String to WeatherData constructor
                        temperature,
                        humidity,
                        precipitation
                );
                weatherData.setDescription(description);

                // Convert timestamp to a local date without time (dateKey is used for grouping)
                String dateKey = java.time.Instant.ofEpochSecond(dt)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate()
                        .toString();

                // Group data by date
                dailyDataMap.computeIfAbsent(dateKey, k -> new ArrayList<>()).
                        add(weatherData);
            }

            // Summarize daily data
            for (Map.Entry<String, List<WeatherData>> entry : dailyDataMap.
                    entrySet()) {
                String date = entry.getKey();
                List<WeatherData> dailyData = entry.getValue();

                // Calculate daily averages
                double avgTemp = dailyData.stream().mapToDouble(
                        WeatherData::getTemperature).average().orElse(0.0);
                double avgHumidity = dailyData.stream().mapToDouble(
                        WeatherData::getHumidity).average().orElse(0.0);
                double totalPrecipitation = dailyData.stream().mapToDouble(
                        WeatherData::getPrecipitation).sum();

                String description = dailyData.get(0).getDescription();

                // Create summary WeatherData for the day
                WeatherData dailySummary = new WeatherData(
                        String.valueOf(dailyData.get(0).getDateTime().
                                toEpochSecond(java.time.ZoneOffset.UTC)), // Pass Unix timestamp here
                        avgTemp,
                        avgHumidity,
                        totalPrecipitation
                );
                dailySummary.setDescription(description);

                weatherDataList.add(dailySummary);
            }


        } catch (Exception e) {
            System.err.println("Error parsing weather data: " + e.getMessage());
            throw new RuntimeException("Failed to parse weather data: " + e.
                    getMessage(), e);
        }
        return weatherDataList;
    }

    private String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int responseCode = conn.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                throw new RuntimeException("Error: " + errorResponse.toString());
            }
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            conn.disconnect();
        }
    }
}
