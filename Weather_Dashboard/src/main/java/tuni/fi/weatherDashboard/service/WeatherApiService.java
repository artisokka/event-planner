package com.eventplanner.dashboard.service;

import com.eventplanner.dashboard.model.DataModel;
import com.eventplanner.dashboard.model.WeatherData;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import java.util.Properties;

public class WeatherApiService {

    private static final String BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather";
    private String API_KEY;

    public WeatherApiService() {
        loadApiKey();
    }

    private void loadApiKey() {
        try (InputStream input = getClass().getClassLoader().
                getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);
            API_KEY = prop.getProperty("api.key");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public DataModel fetchWeatherData(String cityName) {
        WeatherData weatherData = null;

        try {
            String apiUrl = String.format("%s?q=%s&units=metric&appid=%s",
                    BASE_URL, cityName, API_KEY);
            String jsonResponse = sendGetRequest(apiUrl);
            weatherData = parseWeatherData(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return a DataModel with the single WeatherData object
        return new DataModel(weatherData != null ? List.of(weatherData)
                : new ArrayList<>());
    }

    private String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " +
                    responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.
                getInputStream()));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        conn.disconnect();
        return response.toString();
    }

    public WeatherData parseWeatherData(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Extracting data from response
        long dt = jsonObject.getLong("dt"); // Unix timestamp
        String dateTime = String.valueOf(dt); // String representation for constructor

        double temperature = jsonObject.getJSONObject("main").getDouble("temp");
        double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
        double precipitation = jsonObject.has("rain") ? jsonObject.
                getJSONObject("rain").optDouble("1h", 0) : 0; // Default to 0 if no data for now

        // Create and return a WeatherData object
        return new WeatherData(dateTime, temperature, humidity, precipitation);
    }
}
