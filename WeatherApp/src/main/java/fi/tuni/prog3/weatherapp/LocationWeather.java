/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;

/**
 * A Class for storing weather data of one location.
 * @author Valma Haavisto, Aarni Akkala
 */

public class LocationWeather {
    HashMap<LocalDateTime, Weather> forecast;
    ArrayList<LocalDateTime> days = new ArrayList<>();
    Weather currentWeather;
    String city_name;
    String units;
    

    public LocationWeather(HashMap<LocalDateTime, Weather> forecast, Weather currentWeather) {
        this.forecast = forecast;
        this.currentWeather = currentWeather;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
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
    /**
     * Gets the forecast hours that are on the same day as dateTime
     * @param day The day that we are interesetd in. The hour doesn't matter.
     * @return All Weather forecasts that are within the given day.
     */
    public HashMap<LocalDateTime, Weather> get_certain_day_weather(LocalDateTime day) {
        HashMap<LocalDateTime, Weather> weathers = new HashMap<>();

        // Check each forecast hour and see which ones have the same day as "day"
        for (HashMap.Entry<LocalDateTime, Weather> entry : forecast.entrySet()) {
            LocalDateTime forecastDateTime = entry.getKey();
            if (forecastDateTime.toLocalDate().equals(day.toLocalDate())) {
                weathers.put(forecastDateTime, entry.getValue());
            }
        }

        return weathers;
    }
    /**
     * Returns LocalDateTime list of days that a forecast is saved of.
     * @return List of different days saved. Time is atStartOfDay().
     */
    public ArrayList<LocalDateTime> getDays() {
        if (days.isEmpty()) {
            // Iterate over the keys (LocalDateTime) in the forecast
            for (LocalDateTime dateTime : forecast.keySet()) {
                // Extract the date part from each LocalDateTime
                LocalDateTime day = dateTime.toLocalDate().atStartOfDay();

                // Add the day to the ArrayList if it's not already present
                if (!days.contains(day)) {
                    days.add(day);
                }
            }
        }
        
        return days;
    }
}
