/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.time.LocalDateTime;

/**
 * A class for storing weather data of one time of on one location
 * Author: Aarni Akkala
 */
public class Weather {
    
    /**
     * Date and time of the weather data.
     */
    private LocalDateTime date;

    /**
     * Location name, for example, Tampere.
     */
    private String location;

    /**
     * Country code, for example, FI.
     */
    private String country;

    /**
     * Geographical coordinates of the location.
     */
    private Coord coord;

    /**
     * Minimum temperature in Celsius or Fahrenheit.
     */
    private double temp_min;

    /**
     * Maximum temperature in Celsius or Fahrenheit.
     */
    private double temp_max;

    /**
     * Current temperature in Celsius or Fahrenheit.
     */
    private double current_temp;

    /**
     * "Feels like" temperature in Celsius or Fahrenheit.
     */
    private double feels_like;

    /**
     * Wind speed in m/s or mi/h.
     */
    private double wind_speed;

    /**
     * Wind direction in degrees.
     */
    private double wind_direction;

    /**
     * Wind gust speed in m/s or mi/h.
     */
    private double wind_gust;

    /**
     * Rainfall in mm.
     */
    private double rain;

    /**
     * Weather ID, for example, 800.
     */
    private int weather_id;

    /**
     * Weather description, for example, "broken clouds".
     */
    private String description;

    /**
     * Humidity level.
     */
    private double humidity;

    /**
     * Constructs a Weather object with the specified weather data.
     *
     * @param date           Date and time of the weather data.
     * @param location       Location name.
     * @param country        Country code.
     * @param coord          Geographical coordinates.
     * @param temp_min       Minimum temperature.
     * @param temp_max       Maximum temperature.
     * @param current_temp   Current temperature.
     * @param feels_like     "Feels like" temperature.
     * @param wind_speed     Wind speed.
     * @param wind_direction Wind direction.
     * @param wind_gust      Wind gust speed.
     * @param rain           Rainfall.
     * @param weather_id     Weather ID.
     * @param description    Weather description.
     * @param humidity       Humidity level.
     */
    public Weather(LocalDateTime date, String location, String country, 
            Coord coord, double temp_min, double temp_max, double current_temp, 
            double feels_like, double wind_speed, double wind_direction, 
            double wind_gust, double rain, int weather_id, String description, 
            double humidity) {
        this.date = date;
        this.location = location;
        this.country = country;
        this.coord = coord;
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
        this.humidity = humidity;
    }

    /**
     * Gets the geographical coordinates.
     *
     * @return Geographical coordinates.
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     * Gets the date and time of the weather data.
     *
     * @return Date and time.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Gets the location name.
     *
     * @return Location name.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the country code.
     *
     * @return Country code.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the minimum temperature.
     *
     * @return Minimum temperature.
     */
    public double getTemp_min() {
        return temp_min;
    }

    /**
     * Gets the maximum temperature.
     *
     * @return Maximum temperature.
     */
    public double getTemp_max() {
        return temp_max;
    }

    /**
     * Gets the current temperature.
     *
     * @return Current temperature.
     */
    public double getCurrent_temp() {
        return current_temp;
    }

    /**
     * Gets the "feels like" temperature.
     *
     * @return "Feels like" temperature.
     */
    public double getFeels_like() {
        return feels_like;
    }

    /**
     * Gets the wind speed.
     *
     * @return Wind speed.
     */
    public double getWind_speed() {
        return wind_speed;
    }

    /**
     * Gets the wind direction.
     *
     * @return Wind direction.
     */
    public double getWind_direction() {
        return wind_direction;
    }

    /**
     * Gets the wind gust speed.
     *
     * @return Wind gust speed.
     */
    public double getWind_gust() {
        return wind_gust;
    }

    /**
     * Gets the rainfall.
     *
     * @return Rainfall.
     */
    public double getRain() {
        return rain;
    }

    /**
     * Gets the weather ID.
     *
     * @return Weather ID.
     */
    public int getWeather_id() {
        return weather_id;
    }

    /**
     * Gets the weather description.
     *
     * @return Weather description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the humidity level.
     *
     * @return Humidity level.
     */
    public double getHumidity() {
        return humidity;
    }
}
