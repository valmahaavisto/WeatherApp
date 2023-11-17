/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.HashMap;
import java.util.Map;

/**
 * A Class with methods to make data processing between API calls and GUI.
 */

public class Events implements iEvents{
    
    // TODO: store favorites some way
    // TODO: store current location some way
    
    API api;

    @Override
    public void startup() {
        // This is called when interacting with API interface/class
        api = new API();
        api.startup();
    }

    @Override
    public void shut_down() {
        api.shut_down();
    }

    @Override
    public Map<String, Coord> search(String input) {
        
        try {
                
            Map<String, Coord> locations = api.look_up_locations(input); 

            Map<String, Coord> top_5 = new HashMap<>();

            //TODO: criteria for selection
            int i = 0;
            for(Map.Entry<String, Coord> entry : locations.entrySet()) {
                while (i < 6) {
                    top_5.put(entry.getKey(), entry.getValue());
                    i++;
                }
            }

            return top_5;
        } catch(APICallUnsuccessfulException e) {
            // TODO: handle this exception
            return null;
        }
    }

    @Override
    public HashMap<String, Coord> add_favorite(Coord latlong) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public HashMap<String, Coord> get_favourites(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LocationWeather get_weather(Coord latlong, String units) throws InvalidUnitsException {
        
        try {
            Weather weather = api.get_current_weather(latlong, units);
            System.out.print(weather.getDate());
            // TODO: fix this because interface call changed.
            return null;
        } catch(APICallUnsuccessfulException e) {
            // TODO: handle this exception
            return null;
        }
    } 
}
