package com.example.app02;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText cityEditText;
    private Button searchButton, saveButton;
    private TextView cityNameTextView, temperatureTextView, weatherDescriptionTextView, humidityTextView, windTextView;
    private ImageView cityImageView;
    private ProgressBar progressBar;
    private RecyclerView savedLocationsRecyclerView;

    private SavedLocationAdapter adapter;
    private SavedLocationDao savedLocationDao;
    private WeatherResponse currentWeather;

    private static final String WEATHER_API_KEY = "b053e3b9ff33cb77be24b8797e55f328";
    private static final String IMAGE_API_KEY = "49679766-665105e18519b5f3ca69b9421";

    private ApiService weatherService;
    private ApiService imageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        // Retrofit for both services
        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherService = weatherRetrofit.create(ApiService.class);

        Retrofit imageRetrofit = new Retrofit.Builder()
                .baseUrl("https://pixabay.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        imageService = imageRetrofit.create(ApiService.class);

        // Room DB setup
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "weather-db").build();
        savedLocationDao = db.savedLocationDao();

        loadSavedLocations();

        searchButton.setOnClickListener(v -> {
            String city = cityEditText.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeather(city);
                fetchCityImage(city);
            } else {
                Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(v -> {
            if (currentWeather != null) {
                SavedLocation location = new SavedLocation(
                        currentWeather.name,
                        currentWeather.main.temp,
                        currentWeather.weather[0].description
                );
                Executors.newSingleThreadExecutor().execute(() -> {
                    savedLocationDao.insert(location);
                    runOnUiThread(this::loadSavedLocations);
                });
                Toast.makeText(this, "Location saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No weather data to save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        cityEditText = findViewById(R.id.cityEditText);
        searchButton = findViewById(R.id.searchButton);
        saveButton = findViewById(R.id.saveButton);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windTextView = findViewById(R.id.windTextView);
        cityImageView = findViewById(R.id.cityImageView);
        progressBar = findViewById(R.id.progressBar);
        savedLocationsRecyclerView = findViewById(R.id.savedLocationsRecyclerView);
        savedLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchWeather(String city) {
        progressBar.setVisibility(View.VISIBLE);
        Call<WeatherResponse> call = weatherService.getWeather(city, "metric", WEATHER_API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    currentWeather = response.body();
                    updateUI(currentWeather);
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCityImage(String city) {
        Call<ImageResponse> call = imageService.getCityImage(city, "photo", IMAGE_API_KEY);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().hits.length > 0) {
                    String imageUrl = response.body().hits[0].webformatURL;
                    Picasso.get().load(imageUrl).into(cityImageView);
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Image load failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(WeatherResponse weather) {
        cityNameTextView.setText(weather.name);
        temperatureTextView.setText(String.format("%.1f°C", weather.main.temp));
        weatherDescriptionTextView.setText(weather.weather[0].description);
        humidityTextView.setText("Humidity: " + weather.main.humidity + "%");
        windTextView.setText("Wind: " + weather.wind.speed + " m/s");
    }

    private void loadSavedLocations() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<SavedLocation> savedList = savedLocationDao.getAll();
            runOnUiThread(() -> {
                adapter = new SavedLocationAdapter(savedList, location ->
                        Toast.makeText(MainActivity.this, "Clicked: " + location.getCityName(), Toast.LENGTH_SHORT).show()
                );
                savedLocationsRecyclerView.setAdapter(adapter);
            });
        });
    }
}
