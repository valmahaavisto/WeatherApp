/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import java.net.URL;
import java.io.InputStreamReader;
import com.google.gson.*;
import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * A Class for storing weather data of one day.
 */

public class API implements iAPI {
    
    final String API_KEY = "25611dde424220be991fce1c7eefa21f";
    
    final String URL_BEGINNING = "https://api.openweathermap.org/data/2.5/";
    
    final String NOW = "weather?";
    
    private StringBuilder get_data_from_api(String url_string) {
        
        StringBuilder response = new StringBuilder();
        
        try {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            
            int response_code = connection.getResponseCode();
            
            
            if (response_code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                
                String line;
                while((line=reader.readLine())!=null) {
                    response.append(line);
                }
                
                reader.close();
               
                
            } else {
                // System.out.println("Error response code: " + response_code);
                return null;
            }
                    
                    
        } catch (IOException e){
            return null;
        }
        return response;
    }

    @Override
    public void startup() {
        
    }

    @Override
    public void shut_down() {
        
    }

    @Override
    public HashMap<String, Coord> look_up_locations(String loc) 
            throws APICallUnsuccessfulException {
        StringBuilder api_data = get_data_from_api(
                "https://api.openweathermap.org/geo/1.0/direct?q=" 
                        + loc + "&appid=" + API_KEY+ "&limit=5");
        
        if (api_data == null) {
            throw new APICallUnsuccessfulException("Unable to connect to API");
        }
        
        String json_data_string = api_data.toString();
        
        JsonArray jsonArray  = new Gson().fromJson(json_data_string, JsonArray.class);
        
        HashMap<String, Coord> locations = new HashMap();
        
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String country = jsonObject.get("country").getAsString();
            String state = jsonObject.get("country").getAsString();
            double lat = jsonObject.getAsJsonPrimitive("lat").getAsDouble();
            double lon = jsonObject.getAsJsonPrimitive("lon").getAsDouble();
            Coord coord = new Coord(lat, lon);
            locations.put(name+","+country, coord);
        }
        return locations;
    }

    @Override
    public Weather get_current_weather(Coord coords, String units)
            throws InvalidUnitsException, APICallUnsuccessfulException{
        // get data as StringBuilder from the API https
        StringBuilder api_data = get_data_from_api(URL_BEGINNING + NOW + 
                                                "lat="+coords.getLat() +
                                                "&lon="+coords.getLon() + 
                                                "&appid=" + API_KEY + 
                                                "&units=" + units);
        if (api_data == null) {
            throw new APICallUnsuccessfulException("Unable to connect to API");
        }
        
        String json_data_string = api_data.toString();
        
        JsonObject jsonObject = new Gson().fromJson(json_data_string, JsonObject.class);
        
        // Check for errors in the API response
        if (jsonObject.has("cod") && jsonObject.get("cod").getAsInt() != 200) {
            throw new APICallUnsuccessfulException(
                    "Unable to retrieve the requested data from API");
        }

        // Check that essential data is present in the JSON response
        if (!jsonObject.has("name") || !jsonObject.has("main") ||
                !jsonObject.has("wind") || !jsonObject.has("dt") ||
                !jsonObject.has("weather")) {
            throw new APICallUnsuccessfulException(
                    "Unable to retrieve the requested data from API");
        }
        
        String location = jsonObject.getAsJsonPrimitive("name").getAsString();

        JsonObject main = jsonObject.getAsJsonObject("main");
        double current_temp = main.has("temp") ? main.getAsJsonPrimitive("temp").getAsDouble() : Double.NaN;
        double feels_like = main.has("feels_like") ? main.getAsJsonPrimitive("feels_like").getAsDouble() : Double.NaN;
        double temp_min = main.has("temp_min") ? main.getAsJsonPrimitive("temp_min").getAsDouble() : Double.NaN;
        double temp_max = main.has("temp_max") ? main.getAsJsonPrimitive("temp_max").getAsDouble() : Double.NaN;

        JsonObject wind = jsonObject.getAsJsonObject("wind");
        double wind_speed = wind.has("speed") ? wind.getAsJsonPrimitive("speed").getAsDouble() : Double.NaN;
        double wind_direction = wind.has("deg") ? wind.getAsJsonPrimitive("deg").getAsDouble() : Double.NaN;
        double wind_gust = wind.has("gust") ? wind.getAsJsonPrimitive("gust").getAsDouble() : Double.NaN;

        long dt = jsonObject.getAsJsonPrimitive("dt").getAsLong();
        Date date = new Date(dt * 1000);

        JsonArray weather_array = jsonObject.getAsJsonArray("weather");
        if (weather_array == null || weather_array.size() == 0) {
            throw new APICallUnsuccessfulException("Unable to connect to API");
        }
        JsonObject weather_object = weather_array.get(0).getAsJsonObject();
        Integer weather_id = weather_object.has("id") ? weather_object.getAsJsonPrimitive("id").getAsInt() : -1;
        String description = weather_object.has("description") ? weather_object.getAsJsonPrimitive("description").getAsString() : "N/A";

        Weather current_weather = new Weather(date, location, "null", temp_min, temp_max, current_temp,
                feels_like, wind_speed, wind_direction, wind_gust, 0, weather_id, description);

        
        return current_weather;
          
    }

    @Override
    public ArrayList<Weather> get_forecast(Coord coordinates, String units)
            throws InvalidUnitsException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
