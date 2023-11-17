/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.time.LocalDateTime;

import java.util.HashMap;

/**
 * A Class for storing weather data of one location.
 */

public class LocationWeather {
    HashMap<LocalDateTime, Weather> forecast;
    Weather currentWeather;

    public LocationWeather(HashMap<LocalDateTime, Weather> forecast, Weather currentWeather) {
        this.forecast = forecast;
        this.currentWeather = currentWeather;
    }

    public HashMap<LocalDateTime, Weather> getForecast() {
        return forecast;
    }

    public void setForecast(HashMap<LocalDateTime, Weather> forecast) {
        this.forecast = forecast;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }
    
    public HashMap<LocalDateTime, Weather> get_certain_day_weather(LocalDateTime dateTime) {
        HashMap<LocalDateTime, Weather> weathers = new HashMap<>();

        for (HashMap.Entry<LocalDateTime, Weather> entry : forecast.entrySet()) {
            LocalDateTime forecastDateTime = entry.getKey();
            if (forecastDateTime.toLocalDate().equals(dateTime.toLocalDate())) {
                weathers.put(forecastDateTime, entry.getValue());
            }
        }

        return weathers;
    }
}
