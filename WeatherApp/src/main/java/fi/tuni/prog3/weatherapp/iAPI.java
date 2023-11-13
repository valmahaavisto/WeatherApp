/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for extracting data from the OpenWeatherMap API.
 */
public interface iAPI {
    
    /**
     * Operations that need to be done when starting program
     */
    public void startup();
    
    /**
     * Operations that need to be done when shutting program
     */
    public void shut_down();
    
    String API_KEY = "25611dde424220be991fce1c7eefa21f";
    
    /**
     * Searches possible locations matching searchword
     * @param loc Name of the location for which coordinates should be fetched.
     * @return locations that match searchword and their coords
     */
    public Map<String, Coord> look_up_locations(String loc);
    
    /**
     * Returns the current weather for the given coordinates.
     * @param coordinates of the location.
     * @param units options: "imperial" or "metric"
     * @return Weather object of current day. 
     * @throws InvalidUnitsException when units are wrong
     */
    public Weather get_current_weather(Coord coordinates, String units) throws InvalidUnitsException;

    /**
     * Returns a forecast for the given coordinates.
     * @param coordinates of the location.
     * @param units
     * @return String.
     * @throws InvalidUnitsException when units are wrong
     */
    public ArrayList<Weather> get_forecast(Coord coordinates, String units) throws InvalidUnitsException;
}
