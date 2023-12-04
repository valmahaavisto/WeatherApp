/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

/**
* Interface with methods to make data processing between API calls and GUI.
* @author Valma Haavisto, Aarni Akkala
*/
public interface iEvents {
    
    /**
     * Operations that need to be done when starting program
     * Calls API startup()
     * @throws java.io.IOException
     */
    public void startup() throws IOException;
    
    /**
     * Operations that need to be done when shutting program.
     * Saves current location and favorites to file.
     * Calls API shut_down()
     * @throws java.io.IOException
     */
    public void shut_down() throws IOException;

    /**
     * @return LocationWeather of the place that was shown 
     * when the app was closed last time
     * @throws fi.tuni.prog3.exceptions.InvalidUnitsException
     * @throws fi.tuni.prog3.exceptions.APICallUnsuccessfulException
     */
    public LocationWeather get_last_weather() throws InvalidUnitsException, APICallUnsuccessfulException;
    
    /**
     * Fetch first 5 search results that match the searchphrase the best.
     * @param input The text in searchbox
     * @return  Alphabetical list of locations in the form:
     * "location,country_prefix" and Coordinates.
     * @throws fi.tuni.prog3.exceptions.APICallUnsuccessfulException
     */
    public TreeMap<String, Coord> search(String input) throws APICallUnsuccessfulException;
       
    /**
     * Updates favorite information of given location
     * @param latlong Coordinates of the location
     * @param name name of the place
     * @return container of favorite locations and coords
     */
    public HashMap<String, Coord> add_favorite(Coord latlong, String name);
    
    /**
     * Removes location from favorites
     * @param latlong Coordinates of the location
     * @param name name of the place
     * @return container of favorite locations and coords
     */
    public HashMap<String, Coord> remove_favorite(Coord latlong, String name);
    
    /**
     * Searches if coordinates are marked as favorite
     * @param latlong Coordinates of the location
     * @return True -> is favoite, false -> not favorite
     */
    public boolean is_favorite(Coord latlong);
    
    /**
     * Returns container of favorite locations and coords
     * @return container of favorite locations and coords
     */
    public HashMap<String, Coord> get_favourites();
    
    /**
     * 
     * @return container of last 5 searched locations
     */
    public HashMap<String, Coord> get_search_history();
    
    /**
     * Gets locations weather information and saves it to a LocationWeather object.
     * @param latlong Coordinates of the place
     * @param units options: "imperial" or "metric"
     * @return Current day weather information
     * @throws fi.tuni.prog3.exceptions.InvalidUnitsException
     * @throws fi.tuni.prog3.exceptions.APICallUnsuccessfulException
     */
    public LocationWeather get_weather(Coord latlong, String units) throws InvalidUnitsException, APICallUnsuccessfulException;

}
