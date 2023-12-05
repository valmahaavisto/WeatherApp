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
    private double tempMin;

    /**
     * Maximum temperature in Celsius or Fahrenheit.
     */
    private double tempMax;

    /**
     * Current temperature in Celsius or Fahrenheit.
     */
    private double currentTemp;

    /**
     * "Feels like" temperature in Celsius or Fahrenheit.
     */
    private double feelsLike;

    /**
     * Wind speed in m/s or mi/h.
     */
    private double windSpeed;

    /**
     * Wind direction in degrees.
     */
    private double windDirection;

    /**
     * Wind gust speed in m/s or mi/h.
     */
    private double windGust;

    /**
     * Rainfall in mm.
     */
    private double rain;

    /**
     * Weather ID, for example, 800.
     */
    private int weatherID;

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
     * @param tempMin       Minimum temperature.
     * @param tempMax       Maximum temperature.
     * @param currentTemp   Current temperature.
     * @param feelsLike     "Feels like" temperature.
     * @param windSpeed     Wind speed.
     * @param windDirection Wind direction.
     * @param windGust      Wind gust speed.
     * @param rain           Rainfall.
     * @param weatherID     Weather ID.
     * @param description    Weather description.
     * @param humidity       Humidity level.
     */
    public Weather(LocalDateTime date, String location, String country, 
            Coord coord, double tempMin, double tempMax, double currentTemp, 
            double feelsLike, double windSpeed, double windDirection, 
            double windGust, double rain, int weatherID, String description, 
            double humidity) {
        this.date = date;
        this.location = location;
        this.country = country;
        this.coord = coord;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.currentTemp = currentTemp;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.windGust = windGust;
        this.rain = rain;
        this.weatherID = weatherID;
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
    public double getTempMin() {
        return tempMin;
    }

    /**
     * Gets the maximum temperature.
     *
     * @return Maximum temperature.
     */
    public double getTempMax() {
        return tempMax;
    }

    /**
     * Gets the current temperature.
     *
     * @return Current temperature.
     */
    public double getCurrentTemp() {
        return currentTemp;
    }

    /**
     * Gets the "feels like" temperature.
     *
     * @return "Feels like" temperature.
     */
    public double getFeelsLike() {
        return feelsLike;
    }

    /**
     * Gets the wind speed.
     *
     * @return Wind speed.
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Gets the wind direction.
     *
     * @return Wind direction.
     */
    public double getWindDirection() {
        return windDirection;
    }

    /**
     * Gets the wind gust speed.
     *
     * @return Wind gust speed.
     */
    public double getWindGust() {
        return windGust;
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
    public int getWeatherID() {
        return weatherID;
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
