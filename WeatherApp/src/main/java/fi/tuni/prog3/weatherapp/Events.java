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
 */

public class Events implements iEvents {
    
    // store favorites' coordinates and names
    HashMap<String, Coord> favorites = new HashMap<>();
    
    // store current location's coordinates and imperial/metric choice
    Pair<Coord, String> lastWeather = new Pair(new Coord(0.0,0.0), "metric");
    
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
                    System.err.println("Error while creating file favorites.txt");
                    ex.printStackTrace();
                }
            } else {
                System.err.println("Found other IOException(s) when creating "
                        + "favorites.txt");
                
                e.printStackTrace();
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
                    // 'parts[0]' is not a double
                    System.err.println("Invalid latitude format: " + parts[0]);
                }
            } else {
                // lastWeather is empty
                //System.err.println("Latitude is missing or empty.");
            }

        } catch (IOException e) {
             // If the file doesn't exist, create it and empty ArrayList
            if (e instanceof NoSuchFileException) {
                try {
                    Files.createFile(Paths.get(lastWeatherFilePath));
                } catch (IOException ex) {
                    System.err.println("Error while creating file lastWeather.txt");
                    ex.printStackTrace();
                }
            } else {
                System.err.println("Found other IOException(s) when creating "
                        + "lastWeather.txt");
                
                e.printStackTrace();
            } 
        }
    }

    @Override
    public void shut_down() {
        api.shut_down(); 
        
        // read ArrayList favorites to file and empty it
        String favoritesFilePath = "favorites.txt";

        try {
            for (Map.Entry<String, Coord> entry : favorites.entrySet()) {
                String key = entry.getKey();
                Coord value = entry.getValue();
                
                String content = key + ", " + value.getLat() + ", " + value.getLon();
                Files.write(Paths.get(favoritesFilePath), (content + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

            }
            
        } catch (IOException e) {
            System.err.println("File not found: favorites.txt\n");
            
        }
        
        // save lastWeather
        String lastWeatherFilePath = "lastWeather.txt";

        try {
            String content = lastWeather.getKey().getLat()+ ", " +lastWeather.getKey().getLon() 
                    + ", " + lastWeather.getValue();
            
            Files.write(Paths.get(lastWeatherFilePath), content.getBytes());
            
        } catch (IOException e) {
            System.err.println("File not found: lastWeather.txt\n");
        }    
    }

    
    @Override
    public LocationWeather get_last_weather() {
        try {
            return get_weather(lastWeather.getKey(), lastWeather.getValue());
        } catch (InvalidUnitsException ex) {
            System.err.println("Invalid units. Choose 'imperial' or 'metric'.");
            
            return null;
        }
    }
    
    @Override
    public TreeMap<String, Coord> search(String input) {
        
        try {     
            Map<String, Coord> locations = api.look_up_locations(input); 
            Map<String, Coord> top_5 = new HashMap<>();
            int i = 0;
            
            // TODO: does not work
            // get first 5 search results
            for(Map.Entry<String, Coord> entry : locations.entrySet()) {
                while (i < 6) {
                    top_5.put(entry.getKey(), entry.getValue());
                    i++;
                }
            }

            // sort the top_5 by key (city and country name)
            TreeMap<String, Coord> sortedTop5 = new TreeMap<>(top_5);
            
            return sortedTop5;
            
        } catch(APICallUnsuccessfulException e) {
            System.err.println("Unable to get search information.");
            
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
            if (entry.getValue().equals(latlong)) {
                iterator.remove();
            }
        }
        
        return favorites;
    }
    
    @Override
    public boolean is_favorite(Coord latlong) {
        for (Entry<String, Coord> entry : favorites.entrySet()) {
            if (entry.getValue().equals(latlong)) {
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
            // move the searched word to last searched
            lastWeather = new Pair(latlong, units);
            
            Weather weather = api.get_current_weather(latlong, units);
            HashMap <LocalDateTime, Weather> forecast = api.get_forecast(latlong, units);            
            LocationWeather locationWeather = new LocationWeather(forecast, weather);            
            return locationWeather;
            
        } catch(APICallUnsuccessfulException e) {
            System.err.println("Unable to get weather information.");
            
            return null;
        }
    } 
}
