package com.example.app02;


public class WeatherResponse {
    public Main main;
    public Weather[] weather;
    public Wind wind;
    public String name;

    public static class Main {
        public double temp;
        public int humidity;
    }

    public static class Weather {
        public String description;
    }

    public static class Wind {
        public double speed;
    }
}
