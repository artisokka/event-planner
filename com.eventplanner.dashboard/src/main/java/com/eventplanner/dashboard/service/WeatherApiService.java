package com.eventplanner.dashboard.service;

/**
 *
 * @author afeera
 */

import com.eventplanner.dashboard.model.WeatherData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherApiService {
    private static final String API_BASE_URL = "https://opendata.fmi.fi/wfs";
    private static final String API_QUERY = "?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::forecast::harmonie::surface::point::timevaluepair&parameters=temperature,humidity,precipitation1h&";

    private final HttpClient httpClient;

    public WeatherApiService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<WeatherData> fetchWeatherData(String location, LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {
        String url = buildApiUrl(location, startDate, endDate);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return parseWeatherData(response.body());
        } else {
            throw new IOException("API request failed with status code: " + response.statusCode());
        }
    }

    private String buildApiUrl(String location, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        return API_BASE_URL + API_QUERY +
                "place=" + location +
                "&starttime=" + startDate.format(formatter) +
                "T00:00:00Z&endtime=" + endDate.format(formatter) + "T23:59:59Z";
    }

    private List<WeatherData> parseWeatherData(String jsonResponse) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray features = jsonObject.getJSONArray("features");

        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            JSONObject properties = feature.getJSONObject("properties");
            JSONObject timeValuePairs = properties.getJSONObject("timeValuePairs");

            LocalDateTime dateTime = LocalDateTime.parse(properties.getString("time"), DateTimeFormatter.ISO_DATE_TIME);
            double temperature = timeValuePairs.getJSONArray("temperature").getDouble(1);
            double humidity = timeValuePairs.getJSONArray("humidity").getDouble(1);
            double precipitation = timeValuePairs.getJSONArray("precipitation1h").getDouble(1);

            WeatherData weatherData = new WeatherData(dateTime, temperature, humidity, precipitation);
            weatherDataList.add(weatherData);
        }

        return weatherDataList;
    }
}
