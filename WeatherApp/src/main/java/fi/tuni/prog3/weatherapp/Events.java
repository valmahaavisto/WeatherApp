/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A Class with methods to make data processing between API calls and GUI.
 */

public class Events implements iEvents{
    
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
    public boolean add_favorite(String location, Coord latlong) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean remove_favorite(String location, Coord latlong) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Map<String, Coord> get_favourites() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Weather fetch_weather_data(String location, Coord latlong, String units) throws InvalidUnitsException {
        
        try {
            Weather weather = api.get_current_weather(latlong, units);
            System.out.print(weather.getDay());
            return weather;
        } catch(APICallUnsuccessfulException e) {
            // TODO: handle this exception
            return null;
        }
    }

    @Override
    public Weather fetch_day_weather(Date day) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<String> get_days() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Integer> get_every_day_min_temp() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Integer> get_every_day_max_temp() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Integer> get_every_day_description() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
