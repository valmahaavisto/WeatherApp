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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for checking that API call data is good. Junit test file is for
 * separate automatic testing. This is manual testing to see API call data is 
 * what it should. AKA it is not -50C in Buenos Aires.
 * @author Aarni Akkala
 */
public class testAPI {
    
    private static void testCurrentWeather(API api, double lat, double lon, String units) {
    Coord coord = new Coord(lat, lon);
    try {
        Weather w = api.getCurrentWeather(coord, units);

        if (w != null) {
            System.out.println("Location: " + (w.getLocation().equals("null") ? "N/A" : w.getLocation()));
            System.out.println("Current Temperature: " + (Double.isNaN(w.getCurrentTemp()) ? "N/A" : w.getCurrentTemp()));
            System.out.println("Feels Like: " + (Double.isNaN(w.getFeelsLike()) ? "N/A" : w.getFeelsLike()));
            System.out.println("Wind Speed: " + (Double.isNaN(w.getWindSpeed()) ? "N/A" : w.getWindSpeed()));
            System.out.println("Wind Direction: " + (Double.isNaN(w.getWindDirection()) ? "N/A" : w.getWindDirection()));
            System.out.println("Wind Gust: " + (Double.isNaN(w.getWindGust()) ? "N/A" : w.getWindGust()));
            System.out.println("Rain: " + (Double.isNaN(w.getRain()) ? "N/A" : w.getRain()));
            System.out.println("Date: " + (w.getDate() != null ? w.getDate() : "N/A"));
            System.out.println("Weather ID: " + (w.getDescription().equals("null") ? "N/A" : w.getDescription()));
        } else {
            System.out.println("Weather data is null. Check for errors in API call.");
        }
        System.out.println();

    } catch (InvalidUnitsException | APICallUnsuccessfulException e) {
        System.out.println(e.getMessage());
    }
}

    private static void testLookUpLocations(API api, String text) {
        try {
            HashMap<String, Coord> locations = api.lookUpLocations(text);
            
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
    
    private static void testForecast(API api, double lat, double lon, String units) {
        Coord coord = new Coord(lat, lon);
        try {
            HashMap<LocalDateTime, Weather> forecast = api.getForecast(coord, units);

            if (!forecast.isEmpty()) {
                // Sort dates
                List<LocalDateTime> sortedDates = new ArrayList<>(forecast.keySet());
                Collections.sort(sortedDates);

                System.out.println("Forecast for " + coord.getLat() + ":" + coord.getLon() + " (Units: " + units + ")");
                for (LocalDateTime time : sortedDates) {
                    Weather weather = forecast.get(time);

                    System.out.println("Time: " + time);
                    System.out.println("Location: " + (weather.getLocation().equals("null") ? "N/A" : weather.getLocation()));
                    System.out.println("Current Temperature: " + (Double.isNaN(weather.getCurrentTemp()) ? "N/A" : weather.getCurrentTemp()));
                    System.out.println("Feels Like: " + (Double.isNaN(weather.getFeelsLike()) ? "N/A" : weather.getFeelsLike()));
                    System.out.println("Wind Speed: " + (Double.isNaN(weather.getWindSpeed()) ? "N/A" : weather.getWindSpeed()));
                    System.out.println("Wind Direction: " + (Double.isNaN(weather.getWindDirection()) ? "N/A" : weather.getWindDirection()));
                    System.out.println("Wind Gust: " + (Double.isNaN(weather.getWindGust()) ? "N/A" : weather.getWindGust()));
                    System.out.println("Rain: " + (Double.isNaN(weather.getRain()) ? "N/A" : weather.getRain()));
                    System.out.println("Weather ID: " + (weather.getDescription().equals("null") ? "N/A" : weather.getDescription()));
                    System.out.println();
                }
            } else {
                System.out.println("Forecast is empty. Check for errors in API call.");
            }
            System.out.println();

        } catch (InvalidUnitsException | APICallUnsuccessfulException e) {
        System.out.println(e.getMessage());
        }
    }
    
    private static void testGetCityName(API api, String text) {
        try {
            HashMap<String, Coord> locations = api.lookUpLocations(text);
            
            System.out.println("Locations:");
            for (Map.Entry<String, Coord> entry : locations.entrySet()) {
                String name = entry.getKey();
                Coord coord = entry.getValue();
                System.out.println(name+ " | " + 
                        coord.getLat()+ ":" + coord.getLon());
                
                Weather w = api.getCurrentWeather(coord, "metric");
                
                if (w != null) {
                    
                    String city_name = api.getCityName(w.getCoord());
                    
                    
                    
                    System.out.println(text + "->"+ name + "->" + w.getLocation() + "->" + city_name);
                }
                
                
            }
            
        } catch (InvalidUnitsException | APICallUnsuccessfulException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Instantiate API
        API api = new API();

        // api.startup();
        // api.shut_down();
        
        // current weather
        
        testCurrentWeather(api, 60.1695, 24.9354, "metric");  // Helsinki, Finland
        testCurrentWeather(api, 40.7128, -74.0060, "metric");  // New York City, USA
        testCurrentWeather(api, 35.6895, 139.6917, "imperial");  // Tokyo, Japan
        testCurrentWeather(api, -33.8688, 151.2093, "metric");  // Sydney, Australia
        testCurrentWeather(api, -22.9068, -43.1729, "metric");  // Rio de Janeiro, Brazil
        // faulty cases
        testCurrentWeather(api, 300, 300, "metric");
        testCurrentWeather(api, Double.NaN, 30, "imperial");
        testCurrentWeather(api, 0, 0, "freedomUnits");
        
        // look up locations
        testLookUpLocations(api, "tampere");
        testLookUpLocations(api, "a");
        testLookUpLocations(api, "london");
        testLookUpLocations(api, "lontoo");
        testLookUpLocations(api, "lahti");

        // Forecast
        testForecast(api, 60.1695, 24.9354, "metric");  // Helsinki, Finland
        testForecast(api, 40.7128, -74.0060, "imperial");  // New York City, USA
        testForecast(api, 35.6895, 139.6917, "metric");  // Tokyo, Japan
        testForecast(api, -33.8688, 151.2093, "imperial");  // Sydney, Australia
        testForecast(api, -22.9068, -43.1729, "metric");  // Rio de Janeiro, Brazil
        // faulty cases
        testForecast(api, 300, 300, "metric");
        testForecast(api, Double.NaN, 30, "imperial");
        testForecast(api, 0, 0, "freedomUnits");
        
        
        
        //  test that search and getcitname give same name
        testGetCityName(api, "tampere");
        testGetCityName(api, "lahti");
        testGetCityName(api, "parkano");
        testGetCityName(api, "Ei tällästä mestaa oo olemassakaan");
        
        
    }
}
