package com.greenride.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    // Using Open-Meteo (Free, public API, no key required)
    private final String API_URL = "https://api.open-meteo.com/v1/forecast";

    public String getWeatherForecast(double lat, double lon) {
        try {
            String url = String.format("%s?latitude=%s&longitude=%s&current_weather=true", API_URL, lat, lon);
            // Returns the raw JSON string from the external API
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "Weather data currently unavailable.";
        }
    }
}