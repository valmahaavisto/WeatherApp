/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test_classes;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import fi.tuni.prog3.weatherapp.API;
import fi.tuni.prog3.weatherapp.Coord;
import fi.tuni.prog3.weatherapp.Weather;
import java.util.HashMap;
import java.util.Map;

public class testAPI {
    
    private static void testCurrentWeather(API api, double lat, double lon) {
    Coord coord = new Coord(lat, lon);
    try {
        Weather w = api.get_current_weather(coord, "metric");

        if (w != null) {
            System.out.println("Location: " + (w.getLocation().equals("null") ? "N/A" : w.getLocation()));
            System.out.println("Current Temperature: " + (Double.isNaN(w.getCurrent_temp()) ? "N/A" : w.getCurrent_temp()));
            System.out.println("Feels Like: " + (Double.isNaN(w.getFeels_like()) ? "N/A" : w.getFeels_like()));
            System.out.println("Wind Speed: " + (Double.isNaN(w.getWind_speed()) ? "N/A" : w.getWind_speed()));
            System.out.println("Wind Direction: " + (Double.isNaN(w.getWind_direction()) ? "N/A" : w.getWind_direction()));
            System.out.println("Wind Gust: " + (Double.isNaN(w.getWind_gust()) ? "N/A" : w.getWind_gust()));
            System.out.println("Rain: " + (Double.isNaN(w.getRain()) ? "N/A" : w.getRain()));
            System.out.println("Date: " + (w.getDay() != null ? w.getDay() : "N/A"));
            System.out.println("Weather ID: " + (w.getDescription().equals("null") ? "N/A" : w.getDescription()));
        } else {
            System.out.println("Weather data is null. Check for errors in API call.");
        }
        System.out.println();

    } catch (InvalidUnitsException e) {
        System.out.println("Invalid units");
    } catch (APICallUnsuccessfulException e) {
        System.out.println(e.getMessage());
    }
}

    private static void testLookUpLocations(API api, String text) {
        try {
            HashMap<String, Coord> locations = api.look_up_locations(text);
            
            System.out.println("Locations:");
            for (Map.Entry<String, Coord> entry : locations.entrySet()) {
                String name = entry.getKey();
                Coord coord = entry.getValue();
                System.out.println(name+ " | " + 
                        coord.getLat()+ ":" + coord.getLon());
                
            }
            System.out.println();
            
        } catch (APICallUnsuccessfulException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Instantiate API
        API api = new API();

        // api.startup();
        // api.shut_down();
        
        // current weather
        
        testCurrentWeather(api, 60.1695, 24.9354);  // Helsinki, Finland
        testCurrentWeather(api, 40.7128, -74.0060);  // New York City, USA
        testCurrentWeather(api, 35.6895, 139.6917);  // Tokyo, Japan
        testCurrentWeather(api, -33.8688, 151.2093);  // Sydney, Australia
        testCurrentWeather(api, -22.9068, -43.1729);  // Rio de Janeiro, Brazil
        // faulty cases
        testCurrentWeather(api, 300, 300);
        testCurrentWeather(api, Double.NaN, 300);
        
        // look up locations
        
        testLookUpLocations(api, "tampere");
        testLookUpLocations(api, "a");
        testLookUpLocations(api, "london");
        testLookUpLocations(api, "lontoo");
        testLookUpLocations(api, "lahti");
        
        
    }
}
