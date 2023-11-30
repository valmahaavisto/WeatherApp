/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for extracting data from the OpenWeatherMap API.
 * @author Aarni Akkala
 */
public interface iAPI {
    
    /**
     * Searches possible locations matching searchword
     * Preconditions: param loc is not null or empty. 
     * API_KEY is still valid and defined.
     * Postconditions: Succesfull API call returns Map of location name 
     * (in format name,country) and coordinates which are not null. Map may be 
     * empty if there are no matches. Coord has coordinates matching the location.
     * If the data from API is bad or there is no connection 
     * APICallUnsuccessufulException is thrown.
     * @param loc Name of the location for which coordinates should be fetched.
     * @return locations that match searchword and their coords
     * @throws APICallUnsuccessfulException when API call unsuccessfull
     * (no response or data is invalid)
     */
    public Map<String, Coord> look_up_locations(String loc) 
            throws APICallUnsuccessfulException;
    
    /**
     * Returns the current weather for the given coordinates.
     * Forecast arrayLists in Weather object will be empty.
     * Preconditions: coordinates is not null and has lat and lon.
     * units is metric or imperial.
     * Postconditions: Returned Weather object is not null. If units are not
     * metric or imperial InvalidUnitsException is thrown. If the data from API 
     * is bad or there is no connection APICallUnsuccessufulException is thrown.
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
     * Preconditions: coordinates is not null. units is metric or imperial.
     * Postconditions: Returned HashMap is not empty. If units are not
     * metric or imperial InvalidUnitsException is thrown. If the data from API 
     * is bad or there is no connection APICallUnsuccessufulException is thrown.
     * Returned HashMap may be empty if there is no forecast information for the
     * location.
     * @param coordinates of the location.
     * @param units options: "imperial" or "metric"
     * @return Map of timestamps with Weather objects corresponding to same
     * location
     * @throws InvalidUnitsException when units are wrong
     * @throws APICallUnsuccessfulException when API call unsuccessfull
     */
    public HashMap<LocalDateTime, Weather> get_forecast(Coord coordinates, String units) 
            throws InvalidUnitsException, APICallUnsuccessfulException;
    
    /**
     * Returns the name of a city given coordinates. Same name as the same
     * coordinates have when retrieved from look_up_location.
     * Precontions: latlon.lat and latlon.lon are not null. 
     * Postconditions: Returned string may be empty if there is no result for 
     * those coordinates.
     * @param latlon Coordinates of location
     * @return Name of a city that has latlon coordinates in Openweathermap.org.
     * @throws APICallUnsuccessfulException when API call unsuccessfull.
     */
    public String get_city_name(Coord latlon) throws APICallUnsuccessfulException;
}
