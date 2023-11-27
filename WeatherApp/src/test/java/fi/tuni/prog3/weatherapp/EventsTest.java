/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.util.HashMap;
import java.util.TreeMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
        events.startup();
    }

    @AfterEach
    void tearDown() {
        events.shut_down();
    }

    @Test
    void testGetLastWeather() {
        // Ensure that get_last_weather returns a non-null value
        assertNotNull(events.get_last_weather());
    }

    @Test
    void testSearch() {
        // Ensure that search returns a non-null value for a sample input
        assertNotNull(events.search("Helsinki"));
    }

    @Test
    void testAddRemoveFavorite() {
        Coord coordinates = new Coord(60.1695, 24.9354);
        String name = "Helsinki";

        // Add favorite
        events.add_favorite(coordinates, name);
        assertTrue(events.is_favorite(coordinates));

        // Remove favorite
        events.remove_favorite(coordinates, name);
        assertFalse(events.is_favorite(coordinates));
    }

    @Test
    void testGetFavourites() {
        // Ensure that get_favourites returns a non-null value
        assertNotNull(events.get_favourites());
    }

    @Test
    void testGetWeather() throws InvalidUnitsException {
        Coord coordinates = new Coord(60.1695, 24.9354);
        String units = "metric";

        // Ensure that get_weather returns a non-null value for a sample input
        assertNotNull(events.get_weather(coordinates, units));
    }

}
