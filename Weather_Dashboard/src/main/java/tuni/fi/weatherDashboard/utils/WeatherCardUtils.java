package tuni.fi.weatherDashboard.utils;

/**
 *
 * @author janik
 */
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import tuni.fi.weatherDashboard.model.WeatherData;

public class WeatherCardUtils {

    public static VBox createWeatherCard(WeatherData currentWeather) {
        VBox weatherCard = new VBox();
        weatherCard.setSpacing(10);
        weatherCard.setStyle(
                "-fx-background-color: #D0E7F9; -fx-padding: 10; -fx-border-radius: 8;");

        Label locationLabel = new Label(currentWeather.getLocation());
        locationLabel.setFont(Font.font("Arial", 16));

        Text temperatureLabel = new Text(String.format(
                "Temperature: %.1f°C", currentWeather.
                        getTemperature()));
        temperatureLabel.setFont(Font.font("Arial", 14));

        Text humidityLabel = new Text(String.format(
                "Humidity: %.1f%%", currentWeather.
                        getHumidity()));
        humidityLabel.setFont(Font.font("Arial", 14));

        Text descriptionLabel = new Text(String.format(
                "Conditions: %s", currentWeather.
                        getDescription()));
        descriptionLabel.setFont(Font.font("Arial", 14));

        Text precipitationLabel = new Text(String.format(
                "Precipitation: %smm", currentWeather.
                        getPrecipitation()));
        precipitationLabel.setFont(Font.font("Arial", 14));

        Text feelsLikeLabel = new Text(String.format(
                "Feels like: %.1f°C", currentWeather.
                        getFeelsLike()));
        feelsLikeLabel.setFont(Font.font("Arial", 14));

        ImageView icon = new ImageView(
                "/img/sunny.png");  // Placeholder for icon
        icon.setFitWidth(50);
        icon.setFitHeight(50);

        // Fetch weather data asynchronously
        //fetchWeatherDataForLocation(location, weatherCard);
        weatherCard.getChildren().addAll(locationLabel, temperatureLabel,
                feelsLikeLabel,
                humidityLabel, precipitationLabel,
                descriptionLabel, icon);

        return weatherCard;
    }
}
