package tuni.fi.weatherDashboard.utils;

import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import tuni.fi.weatherDashboard.model.DataModel;

/**
 *
 * @author janik
 */
public class LineChartUtils {
    public static XYChart.Series<String, Number> addPoints(DataModel dataModel,
            String param) {

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        // Loop through the weather data
        dataModel.getWeatherDataList().forEach(data -> {
            String formattedDate = DateTimeUtils.formatDateTime(data.
                    getDateTime());

            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>();
            Text label = new Text();

            if (param.equals("max")) {
                series.setName("Max Temperature °C");
                // Create a data point for the chart
                dataPoint = new XYChart.Data<>(
                        formattedDate, data.getTemperature());
                // Create a label to display next to the data point
                label = new Text(String.format("%d°C", (int) data.
                        getTemperature()));
                label.setTranslateX(10);
                label.setTranslateY(-15);
            }

            if (param.equals("min")) {
                series.setName("Min Temperature °C");
                // Create a data point for the chart
                dataPoint = new XYChart.Data<>(
                        formattedDate, data.getMinTemp());
                // Create a label to display next to the data point
                label = new Text(String.format("%d°C", (int) data.
                        getMinTemp()));
                label.setTranslateX(10);
                label.setTranslateY(15);
            }

            if (param.equals("hum")) {
                series.setName("Humidity %");
                // Create a data point for the chart
                dataPoint = new XYChart.Data<>(
                        formattedDate, data.getHumidity());
                // Create a label to display next to the data point
                label = new Text(String.format("%d%%", (int) data.
                        getHumidity()));
                label.setTranslateX(10);
                label.setTranslateY(15);
            }

            label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            // Set the label as the node for the data point
            dataPoint.setNode(label);

            // Add the data point to the series
            series.getData().add(dataPoint);
        });

        return series;
    }
}
