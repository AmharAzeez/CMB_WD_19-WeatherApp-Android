package com.example.app02;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_locations")
public class SavedLocation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cityName;
    private double temperature;
    private String weatherDescription;

    public SavedLocation(String cityName, double temperature, String weatherDescription) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCityName() { return cityName; }
    public double getTemperature() { return temperature; }
    public String getWeatherDescription() { return weatherDescription; }
}