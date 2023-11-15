/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
* Interface with methods to make data processing between API calls and GUI.
*/
public interface iEvents {
    
    /**
     * Operations that need to be done when starting program
     */
    public void startup();
    // API rajapinnan toteuttavan luokan olion tekeminen
    // Kutsu API rajapinnan toteuttavan luokan startup-funktioon
    
    /**
     * Operations that need to be done when shutting program
     */
    public void shut_down();
    // Kutsu API rajapinnan toteuttavan luokan startup-funktioon

    /**
     * Fetch first 5 search results that match the searchphrase the best.
     * @param input The text in searchbox
     * @return  Alphabetical list of locations in the form:
     * "location,country_prefix (lat: xx.xxx, lon: xx.xxx)" and Coordinates.
     */
    public Map<String, Coord> search(String input);
       // Kutsuu API:lta hakutulokseen osuvat paikat ja palauttaa 5 parasta.
       // API:lta tulee N kappaletta osumia (N>5) joista valitaan "parhaat"
       // Jos N<5 näytetään vain ne tai palautetaan tyhjä map, josta seuraa virheteksti 
       
    /**
     * Saves a favorite to a file for use on other executions.
     * @param location Name of the location
     * @param latlong Coordinates of the location
     * @return true if save successful, false if error in saving file.
     */
    public boolean add_favorite(String location, Coord latlong);
    // Tallentaa suosikin tiedostoon. Tallennus tiedostoon kurssin ohjeiden mukaan ohjelmaa sulkiessa.
    
    public boolean remove_favorite(String location, Coord latlong);
    // Poistaa suosikin tiedostosta. Poisto tiedostosta kurssin ohjeiden mukaan ohjelmaa sulkiessa. 
    
    /**
     * Reads favorites from file.
     * @return Map of favorites and their coordinates so they can be 
     * called and displayed.
     */
    public Map<String, Coord> get_favourites();
    // Lukee suosikit tiedostosta kaikki suosikit. Lukeminen kurssin ohjeiden mukaan käynnistyksessä.
    // Suosikit palautetaan lisäämisjärjestyksessä (uusin ensin).  
    
    /**
     * Fetch data about specific location weather now and save forecast
     * information to class
     * @param location Name of the place
     * @param latlong Coordinates of the place
     * @param units options: "imperial" or "metric"
     * @return Current day weather information
     * @throws fi.tuni.prog3.exceptions.InvalidUnitsException
     */
    public Weather fetch_weather_data(String location, Coord latlong, String units) throws InvalidUnitsException;
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
    // Kutsu Weather oliolla getTemps ja tästä min
    
    /**
     * Get maximum temperature of every day available for selected location
     * @return maximum temperature of each day
     */
    public ArrayList<Integer> get_every_day_max_temp();
    // Kutsu Weather oliolla getTemps ja tästä max
    
    /**
     * Get weather description of every day available for selected location
     * See ids at: https://openweathermap.org/weather-conditions
     * @return weather description ids of each day
     */
    public ArrayList<Integer> get_every_day_description();
    
}


