/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.util.Date;
import java.util.HashMap;

/**
 * A Class for storing weather data of one location.
 */

public class LocationWeather {
    HashMap<Date, Weather> forecast;
    Weather currentWeather;

    public LocationWeather(HashMap<Date, Weather> forecast, Weather currentWeather) {
        this.forecast = forecast;
        this.currentWeather = currentWeather;
    }

    public HashMap<Date, Weather> getForecast() {
        return forecast;
    }

    public void setForecast(HashMap<Date, Weather> forecast) {
        this.forecast = forecast;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }
    
    public HashMap<Date, Weather> get_certain_day_weather(Date day) {
        // TODO: Make this return all the weather items in forecast that are on the
        // same day as "day" parameter.
        return null;
    }
}
