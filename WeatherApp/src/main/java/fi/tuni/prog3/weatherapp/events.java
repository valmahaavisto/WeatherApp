/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package fi.tuni.prog3.weatherapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
* Interface with methods to make data processing between API calls and GUI.
*/
public interface events {

    /**
     * Fetch first 5 search results that match the searchphrase the best.
     * @param input The text in searchbox
     * @return  Alphabetical list of locations in the form:
     * "location,country_prefix (lat: xx.xxx, lon: xx.xxx)" and Coordinates.
     */
    public Map<String, Coord> search(String input);
       // Kutsuu API:lta hakutulokseen osuvat paikat ja palauttaa 5 parasta.
       // API:lta tulee N kappaletta osumia (N>5) joista valitaan "parhaat"
       
    /**
     * Saves a favorite to a file for use on other executions.
     * @param location Name of the location
     * @param latlong Coordinates of the location
     * @return true if save successful, false if error in saving file.
     */
    public boolean add_favorite(String location, Coord latlong);
       // Tallentaa suosikin tiedostoon.
    
    /**
     * Reads favorites from file.
     * @return Map of favorites and their coordinates so they can be 
     * called and displayed.
     */
    public Map<String, Coord> get_favourites();
    // Lukee suosikit tiedostosta kaikki suosikit.
    
    /**
     * Fetch data about specific location weather now and save forecast
     * information to class
     * @param location Name of the place
     * @param latlong Coordinates of the place
     * @param units options: "imperial" or "metric"
     * @return Current day weather information
     */
    public Weather fetch_weather_data(String location, Coord latlong, String units);
    // Kutsuu API:lta nykyisen sään ja sääennusteen (kaksi eri API funktiota)
      
    /**
     * Get the weather of currently selected location
     * @param day Date of requested day
     * @return Weather information of the requested day
     */
    public Weather fetch_day_weather(Date day);
    // 
    
    /**
     * Get the days that have been fetched for selected location
     * @return 
     */
    public ArrayList<String> get_days();
    
    /**
     * Get minimum temperature of every day available for selected location
     * @return minimum temperature of each day
     */
    public ArrayList<Integer> get_every_day_min_temp();
    
    /**
     * Get maximum temperature of every day available for selected location
     * @return maximum temperature of each day
     */
    public ArrayList<Integer> get_every_day_max_temp();
    
    /**
     * Get weather description of every day available for selected location
     * See ids at: https://openweathermap.org/weather-conditions
     * @return weather description ids of each day
     */
    public ArrayList<Integer> get_every_day_description();
    
}
