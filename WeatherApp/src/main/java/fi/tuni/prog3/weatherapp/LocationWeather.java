/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;
import javafx.util.Pair;

/**
 * A class for storing weather data (current and forecast )of one location.
 * This class includes the current weather, forecast, city name, and units.
 * Author: Valma Haavisto, Aarni Akkala
 */
public class LocationWeather {
    /**
     * Forecast data for different hours.
     */
    HashMap<LocalDateTime, Weather> forecast;

    /**
     * List of LocalDateTime representing different days in the forecast.
     */
    ArrayList<LocalDateTime> days = new ArrayList<>();

    /**
     * Current weather data.
     */
    Weather currentWeather;

    /**
     * Name of the city.
     */
    String cityName;

    /**
     * Units used for temperature measurements.
     */
    String units;

    /**
     * Constructs a LocationWeather object with the given forecast and current weather data.
     *
     * @param forecast       Forecast data for different hours.
     * @param currentWeather Current weather data.
     */
    public LocationWeather(HashMap<LocalDateTime, Weather> forecast, Weather currentWeather) {
        this.forecast = forecast;
        this.currentWeather = currentWeather;
    }

    /**
     * Gets the units used for temperature measurements.
     *
     * @return Units for temperature measurements.
     */
    public String getUnits() {
        return units;
    }

    /**
     * Sets the units used for temperature measurements.
     *
     * @param units New units for temperature measurements.
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * Gets the name of the city.
     *
     * @return Name of the city.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Sets the name of the city.
     *
     * @param cityName New name of the city.
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Gets the forecast data for different hours.
     *
     * @return Forecast data for different hours.
     */
    public HashMap<LocalDateTime, Weather> getForecast() {
        return forecast;
    }

    /**
     * Sets the forecast data for different hours.
     *
     * @param forecast New forecast data.
     */
    public void setForecast(HashMap<LocalDateTime, Weather> forecast) {
        this.forecast = forecast;
    }

    /**
     * Gets the current weather data.
     *
     * @return Current weather data.
     */
    public Weather getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Sets the current weather data.
     *
     * @param currentWeather New current weather data.
     */
    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    /**
     * Gets the forecast hours that are on the same day as dateTime.
     *
     * @param day The day that we are interested in. The hour doesn't matter.
     * @return All Weather forecasts that are within the given day.
     */
    public HashMap<LocalDateTime, Weather> getCertainDayWeather(LocalDateTime day) {
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
     * Returns a list of LocalDateTime representing days that a forecast is saved for.
     *
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
    
    /**
    * Retrieves the minimum and maximum temperatures for a specific day from 
    * forecast data.
    *
    * @param day The LocalDateTime representing the day for which to 
    * retrieve temperature information.
    * @return A Pair<Double, Double> containing the minimum and maximum 
    * temperatures for the specified day. (In that order)
    */
    public Pair<Double, Double> getDayMinMax(LocalDateTime day) {
        HashMap<LocalDateTime, Weather> times = getCertainDayWeather(day);
        // Initialize min and max temperatures with the first temperature in the map
        double minTemp = Double.POSITIVE_INFINITY ;
        double maxTemp = Double.NEGATIVE_INFINITY; 

        // Iterate through the Weather objects and update min and max temperatures
        for (Weather weather : times.values()) {
            double tempmin = weather.getTempMin();
            double tempmax = weather.getTempMax();
            minTemp = Math.min(minTemp, tempmin);
            maxTemp = Math.max(maxTemp, tempmax);
        }

        return new Pair<>(minTemp, maxTemp);
    }
}
