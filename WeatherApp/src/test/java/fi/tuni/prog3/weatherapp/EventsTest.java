/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.TreeMap;
import javafx.util.Pair;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Junit test for Events class in weatherapp
 * @author Valma Haavisto
 */
class EventsTest {

    private Events events;

    @BeforeEach
    void setUp() throws IOException {
        events = new Events();
        events.startup();
    }

    @Test
    void testGetLastWeather() throws IOException, InvalidUnitsException, APICallUnsuccessfulException {
        // Set up test data
        Coord coord = new Coord(10.0, 20.0);
        String units = "metric";
        events.lastWeather = new Pair<>(coord, units);

        // Create a temporary file for storing the lastWeather data
        Path tempFile = Paths.get("lastWeather_test.txt");
        Files.write(tempFile, String.format("%f, %f, %s", coord.getLat(), coord.getLon(), units).getBytes());

        // Call the method to test
        LocationWeather result = events.getLastWeather();
        Weather currentW = result.getCurrentWeather();
        Coord latlong = currentW.getCoord();
        
        // Verify the result
        assertNotNull(result);
        
        // Coordinates are the same        
        assertEquals(coord, latlong);
        
        // Units are the same
        assertEquals(units, result.getUnits());

        // Clean up: delete the temporary file
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSearch() throws IOException, APICallUnsuccessfulException {
        // Set up test data
        String input = "Turku";
        Map<String, Coord> mockLocations = new HashMap<>();
        mockLocations.put("Turku,FI", new Coord(60.4517531, 22.2670522));

        // Create a temporary file for storing the search results
        Path tempFile = Paths.get("searchResults.txt");
        StringBuilder content = new StringBuilder();
        mockLocations.forEach((key, value) -> content.append(String.format("%s, %f, %f%n", key, value.getLat(), value.getLon())));
        Files.write(tempFile, content.toString().getBytes());

        // Call the method to test
        TreeMap<String, Coord> result = events.search(input);

        for (var entry : result.entrySet()) {
            String result_key = entry.getKey();
            assertNotNull(result_key);
            
            for (var mock : mockLocations.entrySet()) {
                String mock_key = mock.getKey();
                
                // "City,country" is the same
                assertEquals(mock_key, result_key);
            }
        }

        // Clean up: delete the temporary file
        Files.deleteIfExists(tempFile);
    }
    
}