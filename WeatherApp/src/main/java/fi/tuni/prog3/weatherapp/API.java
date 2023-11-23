/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.HashMap;
import java.net.URL;
import java.io.InputStreamReader;
import com.google.gson.*;
import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * A Class for storing weather data of one day.
 */

public class API implements iAPI {
    
    final String API_KEY = "25611dde424220be991fce1c7eefa21f";
    
    private StringBuilder get_data_from_api(String url_string) {
        
        StringBuilder response = new StringBuilder();
        try {
            // Get data from openweathermap.org API
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int response_code = connection.getResponseCode();
            
            if (response_code == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while((line=reader.readLine())!=null) {
                        response.append(line);
                    }
                }
               
                
            } else {
                // If http connection cant be established
                return null;
            }
                    
                    
        } catch (IOException e){
            // Some other error
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
        
        // Get the best 5 matches for searchword from API
        StringBuilder api_data = get_data_from_api(
                "https://api.openweathermap.org/geo/1.0/direct?q=" 
                        + loc + "&appid=" + API_KEY+ "&limit=5");
        
        if (api_data == null) {
            throw new APICallUnsuccessfulException("Unable to connect to API");
        }
        
        // Convert to jsonObject for easier processing
        String json_data_string = api_data.toString();
        JsonArray jsonArray  = new Gson().fromJson(json_data_string, JsonArray.class);
        
        // Go throurh results and append to Map
        HashMap<String, Coord> locations = new HashMap();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String country = jsonObject.get("country").getAsString();
            String state = jsonObject.get("country").getAsString();
            double lat = jsonObject.getAsJsonPrimitive("lat").getAsDouble();
            double lon = jsonObject.getAsJsonPrimitive("lon").getAsDouble();
            Coord coord = new Coord(lat, lon);
            String location_key = name + "," + country;
            if (!locations.containsKey(location_key)) {
                locations.put(location_key, coord);
            }
        }
        return locations;
    }

    @Override
    public Weather get_current_weather(Coord coords, String units)
            throws InvalidUnitsException, APICallUnsuccessfulException{
        
        // Check that valid units
        if (!"metric".equals(units) && !"imperial".equals(units)) {
            throw new InvalidUnitsException(
                    "Invalid units. Choose 'imperial' or 'metric'.");
        }
        
        // Get current weather for given coordinates
        StringBuilder api_data = get_data_from_api(
                "https://api.openweathermap.org/data/2.5/weather?" + 
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
        
        // Get all needed data from API response
        
        String location = jsonObject.getAsJsonPrimitive("name").getAsString();

        // Temperature info
        JsonObject main = jsonObject.getAsJsonObject("main");
        double current_temp = main.has("temp") ? main.getAsJsonPrimitive("temp").getAsDouble() : Double.NaN;
        double feels_like = main.has("feels_like") ? main.getAsJsonPrimitive("feels_like").getAsDouble() : Double.NaN;
        double temp_min = main.has("temp_min") ? main.getAsJsonPrimitive("temp_min").getAsDouble() : Double.NaN;
        double temp_max = main.has("temp_max") ? main.getAsJsonPrimitive("temp_max").getAsDouble() : Double.NaN;

        // Wind info
        JsonObject wind = jsonObject.getAsJsonObject("wind");
        double wind_speed = wind.has("speed") ? wind.getAsJsonPrimitive("speed").getAsDouble() : Double.NaN;
        double wind_direction = wind.has("deg") ? wind.getAsJsonPrimitive("deg").getAsDouble() : Double.NaN;
        double wind_gust = wind.has("gust") ? wind.getAsJsonPrimitive("gust").getAsDouble() : Double.NaN;

        // Time/Date info
        long dt = jsonObject.getAsJsonPrimitive("dt").getAsLong();
        LocalDateTime date = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(dt),
                    ZoneId.systemDefault()
            );

        // Weather description info
        JsonArray weather_array = jsonObject.getAsJsonArray("weather");
        if (weather_array == null || weather_array.size() == 0) {
            throw new APICallUnsuccessfulException("Unable to connect to API");
        }
        JsonObject weather_object = weather_array.get(0).getAsJsonObject();
        Integer id = weather_object.has("id") ? weather_object.getAsJsonPrimitive("id").getAsInt() : -1;
        String description = weather_object.has("description") ? weather_object.getAsJsonPrimitive("description").getAsString() : "N/A";

        // Rain info
        JsonObject rain = jsonObject.getAsJsonObject("rain");
        double rain1h = rain != null && rain.has("1h") ? rain.getAsJsonPrimitive("1h").getAsDouble() : Double.NaN;
        
        // Country info
        JsonObject sys = jsonObject.getAsJsonObject("sys");
        String country = ""; 
        if (sys != null && sys.has("country")) {
            country = sys.getAsJsonPrimitive("country").getAsString();
        }
        
        // Make weather object about current weather and return it
        Weather current_weather = new Weather(
                    date,          
                    location,     
                    country,       
                    temp_min,
                    temp_max,
                    current_temp,
                    feels_like,
                    wind_speed,
                    wind_direction,      
                    wind_gust,
                    rain1h,         
                    id,
                    description
            );
        return current_weather;
          
    }

    @Override
    public HashMap<LocalDateTime, Weather> get_forecast(Coord coords, String units)
            throws InvalidUnitsException, APICallUnsuccessfulException {
        
        // Check that valid units
        if (!"metric".equals(units) && !"imperial".equals(units)) {
            throw new InvalidUnitsException("Invalid units. Choose 'imperial' or 'metric'.");
        }
        
        // Get forecast of given coordinates from API
        StringBuilder api_data = get_data_from_api(
                "https://pro.openweathermap.org/data/2.5/forecast/hourly?"+
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

        // Location info
        JsonObject city = jsonObject.getAsJsonObject("city");
        String city_name = city.getAsJsonPrimitive("name").getAsString();
        String country = city.getAsJsonPrimitive("country").getAsString();
        
        // Go through each hour and collect weather info from that hour
        JsonArray listArray = jsonObject.getAsJsonArray("list");
        HashMap<LocalDateTime,Weather> forecast = new HashMap<>();
        for (JsonElement element : listArray) {
            JsonObject listItem = element.getAsJsonObject();

            // Time
            long dt = listItem.getAsJsonPrimitive("dt").getAsLong();
            LocalDateTime time = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(dt),
                        ZoneId.systemDefault()
                );

            // Temp info
            JsonObject main = listItem.getAsJsonObject("main");
            double temp = main.has("temp") ? main.getAsJsonPrimitive("temp").getAsDouble() : Double.NaN;
            double feels_like = main.has("feels_like") ? main.getAsJsonPrimitive("feels_like").getAsDouble() : Double.NaN;
            double temp_min = main.has("temp_min") ? main.getAsJsonPrimitive("temp_min").getAsDouble() : Double.NaN;
            double temp_max = main.has("temp_max") ? main.getAsJsonPrimitive("temp_max").getAsDouble() : Double.NaN;

            // weather description info
            JsonArray weatherArray = listItem.getAsJsonArray("weather");
            JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();
            int id = weatherObject.has("id") ? weatherObject.getAsJsonPrimitive("id").getAsInt() : -1; 
            String description = weatherObject.has("description") ? weatherObject.getAsJsonPrimitive("description").getAsString() : "";

            // Wind info
            JsonObject wind = listItem.getAsJsonObject("wind");
            double wind_speed = wind.has("speed") ? wind.getAsJsonPrimitive("speed").getAsDouble() : Double.NaN;
            double wind_direction = wind.has("deg") ? wind.getAsJsonPrimitive("deg").getAsDouble() : Double.NaN;
            double wind_gust = wind.has("gust") ? wind.getAsJsonPrimitive("gust").getAsDouble() : Double.NaN;

            // Rain info
            JsonObject rain = listItem.getAsJsonObject("rain");
            double rain1h = rain != null && rain.has("1h") ? rain.getAsJsonPrimitive("1h").getAsDouble() : Double.NaN;

            // Make wetaher object for each hour and append to map
            Weather weather = new Weather(
                    time,
                    city_name,
                    country,
                    temp_min,
                    temp_max,
                    temp,
                    feels_like,
                    wind_speed,
                    wind_direction,
                    wind_gust,
                    rain1h,
                    id,
                    description
            );
            forecast.put(time, weather);
        }

        // Return each forecast hour in list
        return forecast;
        
    }
    
    
}
