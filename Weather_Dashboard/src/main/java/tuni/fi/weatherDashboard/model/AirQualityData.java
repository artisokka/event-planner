package tuni.fi.weatherDashboard.model;

/**
 *
 * @author janik
 */
public class AirQualityData {

    private String city;
    private double pm25;
    private double pm10;
    private double no2;

    public AirQualityData(String city, double pm25, double pm10,
            double no2) {
        this.city = city;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.no2 = no2;
    }

    public String getCity() {
        return city;
    }

    public double getPm25() {
        return pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public double getNo2() {
        return no2;
    }

    @Override
    public String toString() {
        return String.format(
                "City: %s, PM2.5: %.2f, PM10: %.2f, NO2: %.2f",
                city, pm25, pm10, no2);
    }
}
