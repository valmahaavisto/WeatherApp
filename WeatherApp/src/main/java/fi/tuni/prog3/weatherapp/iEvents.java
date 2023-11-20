/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.HashMap;
import java.util.TreeMap;

/**
* Interface with methods to make data processing between API calls and GUI.
*/
public interface iEvents {
    
    /**
     * Operations that need to be done when starting program
     * Calls API startup()
     */
    public void startup();
    // API rajapinnan toteuttavan luokan olion tekeminen
    // Kutsu API rajapinnan toteuttavan luokan startup-funktioon
    
    /**
     * Operations that need to be done when shutting program.
     * Saves current locaiton and favorites to file.
     * Calls API shut_down()
     */
    public void shut_down();
    // Kutsu API rajapinnan toteuttavan luokan startup-funktioon

    /**
     * 
     * @return coordinates of the place that was shown 
     * when the app was closed last time
     */
    public Coord getLastWeather();
    
    /**
     * Fetch first 5 search results that match the searchphrase the best.
     * @param input The text in searchbox
     * @return  Alphabetical list of locations in the form:
     * "location,country_prefix" and Coordinates.
     */
    public TreeMap<String, Coord> search(String input);
       // Kutsuu API:lta hakutulokseen osuvat paikat ja palauttaa 5 parasta.
       // API:lta tulee 5 kappaletta osumia  joista valitaan "parhaat" 5
       // Jos N<5 näytetään vain ne tai palautetaan tyhjä map, josta seuraa virheteksti
       // WeatherApp:ssa
       
    /**
     * Updates favorite information of given location
     * @param latlong Coordinates of the location
     * @param name name of the place
     * @return container of favorite locations and coords
     */
    public HashMap<String, Coord> add_favorite(Coord latlong, String name);
    // Toteuta suosikkien välimuistissa pitäminen parhaaksi katsomallasi tavalla :)
    
    /**
     * Returns container of favorite locations and coords
     * @return container of favorite locations and coords
     */
    public HashMap<String, Coord> get_favourites(); 
    
    /**
     * Gets locations weather information and saves it to a LocationWeather object.
     * @param latlong Coordinates of the place
     * @param units options: "imperial" or "metric"
     * @return Current day weather information
     * @throws fi.tuni.prog3.exceptions.InvalidUnitsException
     */
    public LocationWeather get_weather(Coord latlong, String units) throws InvalidUnitsException;
    // Kutsuu API:lta nykyisen sään ja sääennusteen (kaksi eri API funktiota)

}
