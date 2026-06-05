package com.example.app02;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String city,
            @Query("units") String units,
            @Query("appid") String apiKey
    );

    @GET("api/")
    Call<ImageResponse> getCityImage(
            @Query("q") String city,
            @Query("image_type") String type,
            @Query("key") String apiKey
    );
}