/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Stream;
import javafx.util.Pair;

/**
 * A Class with methods to make data processing between API calls and GUI.
 * @author Valma Haavisto
 */

public class Events implements iEvents {
    
    // store favorites' coordinates and names
    HashMap<String, Coord> favorites = new HashMap<>();
    
    // store current location's coordinates and imperial/metric choice
    Pair<Coord, String> lastWeather = new Pair(null, null);
    
    API api;

    @Override
    public void startup() {
        // This is called when interacting with API interface/class
        api = new API();
        api.startup();
        
        String favoritesFilePath = "favorites.txt";

        try {
            // Open the file and read it line by line using a stream
            try (Stream<String> lines = Files.lines(Paths.get(favoritesFilePath))) {
                lines.forEach(line -> {
                    String[] parts = line.split(", ");
                    String name = parts[0];
                    double lat = Double.parseDouble(parts[1]);
                    double lon = Double.parseDouble(parts[2]);
                    Coord coordinates = new Coord(lat, lon);
                    favorites.put(name, coordinates);
                });
            }

        } catch (IOException e) {
             // If the file doesn't exist, create it and empty ArrayList
            if (e instanceof NoSuchFileException) {
                try {
                    Files.createFile(Paths.get(favoritesFilePath));
                } catch (IOException ex) {
                    handleException("Error while creating file favorites.txt", ex);
                }
            } else {
                handleException("Found other IOException(s) when creating "
                        + "favorites.txt", e);               
            }
        }
        
        
        String lastWeatherFilePath = "lastWeather.txt";
        
        try {
           // Read the string representation of Coord from the file
            String coordString = new String(Files.readAllBytes(Paths.get(lastWeatherFilePath)));
            String[] parts = coordString.split(", ");
            
            // Split the string into latitude and longitude parts
            if (parts.length > 0 && !parts[0].isEmpty()) {
                try {
                    double lat = Double.parseDouble(parts[0]);
                    double lon = Double.parseDouble(parts[1]);
                    String unit = parts[2];

                    lastWeather = new Pair(new Coord(lat, lon), unit);
                } catch (NumberFormatException e) {
                    // 'parts[0]' or 'parts[1]' is not a double
                    handleException("Invalid latitude format: " + parts[0] + ", " + parts[1], e);
                }
            } 

        } catch (IOException e) {
             // If the file doesn't exist, create it and empty ArrayList
            if (e instanceof NoSuchFileException) {
                try {
                    Files.createFile(Paths.get(lastWeatherFilePath));
                } catch (IOException ex) {
                    handleException("Error while creating file lastWeather.txt", ex); 
                }
            } else {
                handleException("Found other IOException(s) when creating "
                        + "lastWeather.txt", e);              
            } 
        }
    }

    @Override
    public void shut_down() {
        
        api.shut_down(); 

        // empty favorites and add ArrayList favorites to it
        String favoritesFilePath = "favorites.txt";

        byte[] emptyBytes = new byte[0];

        try {
            // Write the empty byte array to the file
            Files.write(Paths.get(favoritesFilePath), emptyBytes);
        } catch (IOException ex) {
            handleException("File not found: " + favoritesFilePath + "\n", ex);
        }
        
        try {
            for (Map.Entry<String, Coord> entry : favorites.entrySet()) {
                Coord value = entry.getValue();

                String content = entry.getKey() + ", " + value.getLat() + ", " + value.getLon();
                Files.write(Paths.get(favoritesFilePath), 
                        (content + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                
            }
        } catch (IOException e) {
            handleException("File not found: " + favoritesFilePath + "\n", e);
        }
        
        // save lastWeather
        String lastWeatherFilePath = "lastWeather.txt";

        try {
            if (lastWeather.getKey() != null && lastWeather.getValue() != null) {
                String content = lastWeather.getKey().getLat()+ ", " +lastWeather.getKey().getLon() 
                    + ", " + lastWeather.getValue();
                Files.write(Paths.get(lastWeatherFilePath), content.getBytes());
            } 
            
            
        } catch (IOException e) {
            handleException("File not found: lastWeather.txt\n", e);
        }    
    }

    
    @Override
    public LocationWeather get_last_weather() {
        try {
            LocationWeather w = get_weather(lastWeather.getKey(), lastWeather.getValue());
            // Add the city_name that usually is gotten from search
            String cityname = api.get_city_name(lastWeather.getKey());
            w.setCity_name(cityname);
            String units = lastWeather.getValue();
            w.setUnits(units);
            return w;
            
        } catch (InvalidUnitsException | APICallUnsuccessfulException ex) {
            if (lastWeather.getKey() != null) {
                handleException("Unable to retrieve last weather", ex);
                return null;             
            }
            
            return null;
        }
    }
    
    @Override
    public TreeMap<String, Coord> search(String input) {
        
        try {     
            Map<String, Coord> locations = api.look_up_locations(input); 

            // sort the top_5 by key (city and country name)
            TreeMap<String, Coord> sortedTop5 = new TreeMap<>(locations);
            
            return sortedTop5;
            
        } catch(APICallUnsuccessfulException e) {
            handleException("Unable to get search information", e);
            return null;
        }
    }

    @Override
    public HashMap<String, Coord> add_favorite(Coord latlong, String name) {
        favorites.put(name, latlong);
        
        return favorites;
    }
    
    @Override
    public HashMap<String, Coord> remove_favorite(Coord latlong, String name) {
        // remove by value (location's coordinates)
        Iterator<Entry<String, Coord>> iterator = favorites.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Entry<String, Coord> entry = iterator.next();
            if (entry.getValue().getLon() == latlong.getLon() 
                    && entry.getValue().getLat() == latlong.getLat()) {
                iterator.remove();
            }
        }
        
        return favorites;
    }
    
    @Override
    public boolean is_favorite(Coord latlong) {
        for (Entry<String, Coord> entry : favorites.entrySet()) {
            if (entry.getValue().getLon() == latlong.getLon() 
                    && entry.getValue().getLat() == latlong.getLat()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public HashMap<String, Coord> get_favourites(){
        return favorites;
    }

    @Override
    public LocationWeather get_weather(Coord latlong, String units) throws InvalidUnitsException {
        
        try {           
            lastWeather = new Pair(latlong, units);
            
            Weather weather = api.get_current_weather(latlong, units);
            HashMap <LocalDateTime, Weather> forecast = api.get_forecast(latlong, units);            
            LocationWeather locationWeather = new LocationWeather(forecast, weather);
            
            return locationWeather;
            
        } catch(APICallUnsuccessfulException e) {
            handleException("Unable to retrieve data from API", e);
            return null;
        }
        
        
    } 
    
    private void handleException(String message, Exception ex) {
        System.err.println(message);
        ex.printStackTrace(); // Print the stack trace for debugging purposes
    }
}
