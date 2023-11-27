/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Valma Haavisto
 */
class EventsTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void testStartup() throws IOException {
        // Mocking file read
        Files.write(Path.of("favorites.txt"), "City, 12.34, 56.78".getBytes(), StandardOpenOption.CREATE);

        // Calling the method
        events.startup();

        // Asserting the results
        assertEquals(1, events.favorites.size());
        assertTrue(events.favorites.containsKey("City"));
    }

    @Test
    void testStartupFileNotFound() throws IOException {
        // Calling the method
        events.startup();

        // Asserting the results
        assertTrue(events.favorites.isEmpty());
    }

    @Test
    void testShutDown() throws IOException {
        // Calling the method
        events.shut_down();

        // Asserting the results
        assertTrue(events.favorites.isEmpty());
    }

    @Test
    void testGetLastWeather() throws InvalidUnitsException, APICallUnsuccessfulException {
        // Mocking API calls
        events.api = new TestAPI();

        // Calling the method
        LocationWeather result = events.get_last_weather();

        // Asserting the results
        assertNull(result);
    }

    @Test
    void testSearch() throws APICallUnsuccessfulException {
        // Mocking API calls
        events.api = new TestAPI();

        // Calling the method
        TreeMap<String, Coord> result = events.search("City");

        // Asserting the results
        assertTrue(result.isEmpty());
    }

    @Test
    void testAddFavorite() {
        // Calling the method
        events.add_favorite(new Coord(12.34, 56.78), "City");

        // Asserting the results
        assertEquals(1, events.favorites.size());
        assertTrue(events.favorites.containsKey("City"));
    }

    @Test
    void testRemoveFavorite() {
        // Adding a favorite
        events.add_favorite(new Coord(12.34, 56.78), "City");

        // Calling the method to remove
        events.remove_favorite(new Coord(12.34, 56.78), "City");

        // Asserting the results
        assertTrue(events.favorites.isEmpty());
    }

    @Test
    void testIsFavorite() {
        // Adding a favorite
        events.add_favorite(new Coord(12.34, 56.78), "City");

        // Calling the method
        boolean result = events.is_favorite(new Coord(12.34, 56.78));

        // Asserting the results
        assertTrue(result);
    }

    @Test
    void testIsNotFavorite() {
        // Calling the method
        boolean result = events.is_favorite(new Coord(12.34, 56.78));

        // Asserting the results
        assertFalse(result);
    }

    @Test
    void testGetFavorites() {
        // Adding favorites
        events.add_favorite(new Coord(12.34, 56.78), "City1");
        events.add_favorite(new Coord(34.56, 78.90), "City2");

        // Calling the method
        Map<String, Coord> result = events.get_favourites();

        // Asserting the results
        assertEquals(2, result.size());
        assertTrue(result.containsKey("City1"));
        assertTrue(result.containsKey("City2"));
    }

    @Test
    void testGetWeather() throws InvalidUnitsException, APICallUnsuccessfulException {
        // Mocking API calls
        events.api = new TestAPI();

        // Calling the method
        LocationWeather result = events.get_weather(new Coord(12.34, 56.78), "metric");

        // Asserting the results
        assertNull(result);
    }

    // Internal TestAPI class to simulate API behavior
    private static class TestAPI extends API {
        @Override
        public void startup() {
            // Do nothing
        }

        @Override
        public void shut_down() {
            // Do nothing
        }

        @Override
        public Weather get_current_weather(Coord latlong, String units) {
            return null;
        }

        @Override
        public HashMap<LocalDateTime, Weather> get_forecast(Coord latlong, String units) {
            return null;
        }

        @Override
        public HashMap<String, Coord> look_up_locations(String input) {
            return null;
        }

        @Override
        public String get_city_name(Coord latlong) {
            return null;
        }
    }
}