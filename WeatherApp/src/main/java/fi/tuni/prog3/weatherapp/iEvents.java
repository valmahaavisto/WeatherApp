/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.io.IOException;
import java.util.ArrayList;
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
     * Preconditions: none
     * Postconditions: The API instance (api) is initialized.
     * Favorites are loaded from the "favorites.txt" file into the favorites map.
     * Last weather information is loaded from the "lastWeather.txt" file into the lastWeather pair.
     * Search history is loaded from the "searchHistory.txt" file into the searchHistory list.
     * @throws java.io.IOException
     */
    public void startup() throws IOException;
    
    /**
     * Operations that need to be done when shutting program.
     * Saves current location and favorites to file.
     * Calls API shut_down()
     * Preconditions: none
     * Postconditions: The current weather based on the last weather coordinates and units is returned.
     * The city name and units of the returned LocationWeather object are set.
     * @throws java.io.IOException
     */
    public void shutDown() throws IOException;

    /**
     * Preconditions: none
     * Postconditions: Locations based on the input are looked up using the API.
     * The top 5 locations are sorted and returned in a TreeMap.
     * The input is added to the search history.
     * If the search history size exceeds 5, the oldest element is removed.
     * @return LocationWeather of the place that was shown 
     * when the app was closed last time
     * @throws fi.tuni.prog3.exceptions.InvalidUnitsException
     * @throws fi.tuni.prog3.exceptions.APICallUnsuccessfulException
     */
    public LocationWeather getLastWeather() throws InvalidUnitsException, APICallUnsuccessfulException;
    
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
    public HashMap<String, Coord> addFavorite(Coord latlong, String name);
    
    /**
     * Removes location from favorites
     * @param latlong Coordinates of the location
     * @param name name of the place
     * @return container of favorite locations and coords
     */
    public HashMap<String, Coord> removeFavorite(Coord latlong, String name);
    
    /**
     * Searches if coordinates are marked as favorite
     * @param latlong Coordinates of the location
     * @return True -> is favoite, false -> not favorite
     */
    public boolean isFavorite(Coord latlong);
    
    /**
     * Returns container of favorite locations and coords
     * @return container of favorite locations and coords
     */
    public HashMap<String, Coord> getFavorites();
    
    /**
     * 
     * @return container of last 5 searched locations
     */
    public ArrayList<String> getSearchHistory();
    
    /**
     * Gets locations weather information and saves it to a LocationWeather object.
     * @param latlong Coordinates of the place
     * @param units options: "imperial" or "metric"
     * @return Current day weather information
     * @throws fi.tuni.prog3.exceptions.InvalidUnitsException
     * @throws fi.tuni.prog3.exceptions.APICallUnsuccessfulException
     */
    public LocationWeather getWeather(Coord latlong, String units) throws InvalidUnitsException, APICallUnsuccessfulException;

}
