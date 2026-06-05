package com.example.app02;

public class WeatherData {
    private String cityName;
    private double temperature;
    private String weatherDescription;
    private int humidity;
    private double windSpeed;
    private String imageUrl;

    // Constructor
    public WeatherData(String cityName, double temperature, String weatherDescription,
                       int humidity, double windSpeed, String imageUrl) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getCityName() { return cityName; }
    public double getTemperature() { return temperature; }
    public String getWeatherDescription() { return weatherDescription; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public String getImageUrl() { return imageUrl; }
}
