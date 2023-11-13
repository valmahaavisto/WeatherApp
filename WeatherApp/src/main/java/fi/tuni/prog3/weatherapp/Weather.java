/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.util.ArrayList;
import java.util.Date;

/**
 * A Class for storing weather data of one day.
 */

public class Weather {
    
    private Date day;
    private String location;
    
    private double current_temp;
    private double feels_like;
    private String air_quality;
    private double wind_speed;
    private double wind_direction;
    private double rain;
    private int description;
    
    private ArrayList<Integer> times;
    private ArrayList<Integer> temps;
    private ArrayList<Integer> wind_speeds;
    private ArrayList<Integer> rains;
    private ArrayList<Integer> descriptions;

    public Weather(Date day, String location, double current_temp, 
            double feels_like, String air_quality, double wind_speed, 
            double wind_direction, double rain, int description, 
            ArrayList<Integer> times, ArrayList<Integer> temps, 
            ArrayList<Integer> wind_speeds, ArrayList<Integer> rains, 
            ArrayList<Integer> descriptions) {
        
        this.day = day;
        this.location = location;
        this.current_temp = current_temp;
        this.feels_like = feels_like;
        this.air_quality = air_quality;
        this.wind_speed = wind_speed;
        this.wind_direction = wind_direction;
        this.rain = rain;
        this.description = description;
        this.times = times;
        this.temps = temps;
        this.wind_speeds = wind_speeds;
        this.rains = rains;
        this.descriptions = descriptions;
    }

    public Date getDay() {
        return day;
    }

    public String getLocation() {
        return location;
    }

    public double getCurrent_temp() {
        return current_temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public String getAir_quality() {
        return air_quality;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public double getWind_direction() {
        return wind_direction;
    }

    public double getRain() {
        return rain;
    }

    public int getDescription() {
        return description;
    }

    public ArrayList<Integer> getTimes() {
        return times;
    }

    public ArrayList<Integer> getTemps() {
        return temps;
    }

    public ArrayList<Integer> getWind_speeds() {
        return wind_speeds;
    }

    public ArrayList<Integer> getRains() {
        return rains;
    }

    public ArrayList<Integer> getDescriptions() {
        return descriptions;
    }

    
    

}
