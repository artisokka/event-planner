package tuni.fi.weatherDashboard.service;

import org.json.JSONArray;
import org.json.JSONObject;
import tuni.fi.weatherDashboard.model.AirQualityData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OpenAQService {

    private static final String BASE_URL = "https://api.openaq.org/v2/locations";
    private static final String AQ_API =
            "d5a122ff6325b29ceca2e1b3f710c4020ea6045a8617178adc3fd83f8c57ed23";

    // Method to fetch air quality data by city
    public AirQualityData fetchAirQualityData(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a city name");
        }

        try {
            // Encode the city name
            String encodedCity = URLEncoder.encode(cityName.trim(),
                    StandardCharsets.UTF_8.toString());
            String apiUrl = String.format("%s?city=%s", BASE_URL, encodedCity);

            System.out.println("Fetching air quality data for: " + cityName);
            String jsonResponse = sendGetRequest(apiUrl);

            // Parse the JSON response
            return parseAirQualityData(jsonResponse);

        } catch (Exception e) {
            System.err.println(
                    "Error fetching air quality data for " + cityName + ": " +
                    e.getMessage());
            throw new RuntimeException("Failed to fetch air quality data: " + e.
                    getMessage(), e);
        }
    }

    // Method to send GET request with API key
    private String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Add the API key as a header
        conn.setRequestProperty("X-API-Key", AQ_API);
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int responseCode = conn.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP request failed with code: " +
                    responseCode);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.
                getInputStream()))) {
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

    // Method to parse the air quality data
    private AirQualityData parseAirQualityData(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");

            if (results.isEmpty()) {
                throw new RuntimeException(
                        "No air quality data available for the location");
            }

            JSONObject firstResult = results.getJSONObject(0);
            String city = firstResult.getString("city");
            JSONArray parameters = firstResult.getJSONArray("parameters");

            double pm25 = 0.0;
            double pm10 = 0.0;
            double no2 = 0.0;

            for (int i = 0; i < parameters.length(); i++) {
                JSONObject parameter = parameters.getJSONObject(i);
                String param = parameter.getString("parameter");
                double value = parameter.getDouble("lastValue");

                // Dynamically handle each measurement param
                switch (param) {
                    case "pm25":
                        pm25 = value;
                        break;
                    case "pm10":
                        pm10 = value;
                        break;
                    case "no2":
                        no2 = value;
                        break;
                    default:
                        // For debug
                        System.out.println("Unknown parameter: " + parameter +
                                " with value: " + value);
                }
            }

            // Return the data wrapped in the AirQualityData model
            return new AirQualityData(city, pm25, pm10, no2);

        } catch (Exception e) {
            System.err.println("Error parsing air quality data: " + e.
                    getMessage());
            throw new RuntimeException("Failed to parse air quality data: " + e.
                    getMessage(), e);
        }
    }
}
