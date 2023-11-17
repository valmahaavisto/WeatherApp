/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.time.LocalDateTime;

/**
 * A Class for storing weather data of one day.
 */

public class Weather {
    
    private LocalDateTime date;              // For example: 17/11/2023 17.44
    private String location;       // For example: Tampere
    private String country;        // For example: FI
    
    private double temp_min;       // Celsius or farenheit
    private double temp_max;       // Celsius or farenheit
    private double current_temp;   // Celsius or farenheit
    private double feels_like;     // Celsius or farenheit
    private double wind_speed;     // Celsius or farenheit
    private double wind_direction; // Degrees
    private double wind_gust;      // m/s or freedoms/second? :)
    private double rain;           // mm or mm :)
    private int weather_id;        //For example: 800
    private String description;    // For example: "broken clouds"

    public Weather(LocalDateTime date, String location, String country, double temp_min, 
            double temp_max, double current_temp, double feels_like, 
            double wind_speed, double wind_direction, double wind_gust, 
            double rain, int weather_id, String description) {
        this.date = date;
        this.location = location;
        this.country = country;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.current_temp = current_temp;
        this.feels_like = feels_like;
        this.wind_speed = wind_speed;
        this.wind_direction = wind_direction;
        this.wind_gust = wind_gust;
        this.rain = rain;
        this.weather_id = weather_id;
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getCurrent_temp() {
        return current_temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public double getWind_direction() {
        return wind_direction;
    }

    public double getWind_gust() {
        return wind_gust;
    }

    public double getRain() {
        return rain;
    }

    public int getWeather_id() {
        return weather_id;
    }

    public String getDescription() {
        return description;
    }
    
    
}
