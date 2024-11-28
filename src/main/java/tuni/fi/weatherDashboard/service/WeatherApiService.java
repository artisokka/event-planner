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
import java.util.List;

public class WeatherApiService {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String API_KEY = "b7342cc2f1ed92e5f2a6e98263b04190";

    public WeatherApiService() {
        // Initialize service
        System.out.println("Weather API Service initialized");
    }

    public DataModel fetchWeatherData(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a city name");
        }

        WeatherData weatherData = null;
        try {
            // Clean and encode the city name
            String encodedCity = URLEncoder.encode(cityName.trim(), StandardCharsets.UTF_8.toString());
            String apiUrl = String.format("%s?q=%s&units=metric&appid=%s",
                    BASE_URL, encodedCity, API_KEY);
            
            System.out.println("Fetching weather data for: " + cityName);
            String jsonResponse = sendGetRequest(apiUrl);
            
            weatherData = parseWeatherData(jsonResponse);
            weatherData.setLocation(cityName);
            
            System.out.println("Successfully retrieved weather data for: " + cityName);
        } catch (Exception e) {
            System.err.println("Error fetching weather data for " + cityName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return new DataModel(weatherData != null ? List.of(weatherData) : new ArrayList<>());
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
                throw new RuntimeException(switch (responseCode) {
                    case 401 -> "Invalid API key. Please check your configuration.";
                    case 404 -> "City not found. Please check the city name.";
                    case 429 -> "Too many requests. Please try again later.";
                    default -> "Error: " + errorResponse.toString();
                });
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

    public WeatherData parseWeatherData(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            
            // Extract main weather data
            long dt = jsonObject.getLong("dt");
            JSONObject main = jsonObject.getJSONObject("main");
            
            double temperature = main.getDouble("temp");
            double humidity = main.getDouble("humidity");
            
            // Handle precipitation data
            double precipitation = 0.0;
            if (jsonObject.has("rain")) {
                JSONObject rain = jsonObject.getJSONObject("rain");
                precipitation = rain.optDouble("1h", 0.0);
            } else if (jsonObject.has("snow")) {
                JSONObject snow = jsonObject.getJSONObject("snow");
                precipitation = snow.optDouble("1h", 0.0);
            }

            WeatherData weatherData = new WeatherData(
                String.valueOf(dt),
                temperature,
                humidity,
                precipitation
            );

            // Add weather description
            if (jsonObject.has("weather") && jsonObject.getJSONArray("weather").length() > 0) {
                String description = jsonObject.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");
                weatherData.setDescription(description);
            }

            return weatherData;
        } catch (Exception e) {
            System.err.println("Error parsing weather data: " + e.getMessage());
            throw new RuntimeException("Failed to parse weather data: " + e.getMessage());
        }
    }
}