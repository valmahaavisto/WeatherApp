/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for extracting data from the OpenWeatherMap API.
 */
public interface iAPI {
    
    /**
     * Operations that need to be done when starting program
     */
    public void startup();
    // Hakee ohjelman tilan viimeisimmän käynnistyksen yhteydessä 
    
    /**
     * Operations that need to be done when shutting program
     */
    public void shut_down();
    // Päivittää suosikit -listan
    
    /**
     * Searches possible locations matching searchword
     * @param loc Name of the location for which coordinates should be fetched.
     * @return locations that match searchword and their coords
     * @throws APICallUnsuccessfulException when API call unsuccessfull
     */
    public Map<String, Coord> look_up_locations(String loc) 
            throws APICallUnsuccessfulException;
    
    /**
     * Returns the current weather for the given coordinates.
     * Forecast arrayLists in Weather object will be empty.
     * @param coordinates of the location.
     * @param units options: "imperial" or "metric"
     * @return Weather object of current day. 
     * @throws InvalidUnitsException when units are wrong
     * @throws APICallUnsuccessfulException when API call unsuccessfull
     */
    public Weather get_current_weather(Coord coordinates, String units) 
            throws InvalidUnitsException, APICallUnsuccessfulException;

    /**
     * Returns a forecast for the given coordinates.
     * @param coordinates of the location.
     * @param units
     * @return String.
     * @throws InvalidUnitsException when units are wrong
     * @throws APICallUnsuccessfulException when API call unsuccessfull
     */
    public HashMap<Date, Weather> get_forecast(Coord coordinates, String units) 
            throws InvalidUnitsException, APICallUnsuccessfulException;
}
