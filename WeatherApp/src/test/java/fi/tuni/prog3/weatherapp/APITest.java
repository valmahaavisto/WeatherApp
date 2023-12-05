/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import fi.tuni.prog3.exceptions.APICallUnsuccessfulException;
import fi.tuni.prog3.exceptions.InvalidUnitsException;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Junit test for API class in weatherapp
 * @author Aarni Akkala
 */
public class APITest {
    
    public APITest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of startup method, of class API.
     */
    @Test
    public void testStartup() {

    }

    /**
     * Test of shut_down method, of class API.
     */
    @Test
    public void testShut_down() {

    }

    /**
     * Test of lookUpLocations method, of class API.
     */
    @Test
    public void testLookUpLocations() throws Exception {
        API api = new API();
        String location = "Helsinki";
        try {
            HashMap<String, Coord> locations = api.lookUpLocations(location);
            assertNotNull(locations);
            assertFalse(locations.isEmpty());
        } catch (APICallUnsuccessfulException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test of getCurrentWeather method, of class API.
     */
    @Test
    public void testGetCurrentWeather() throws Exception {
        API api = new API();
        
        // Test good inputs
        Coord coords = new Coord(60.1695, 24.9354); // Example coordinates for Helsinki
        String units = "metric";
        try {
            Weather currentWeather = api.getCurrentWeather(coords, units);
            assertNotNull(currentWeather);
            assertEquals("Helsinki", currentWeather.getLocation());
        } catch (APICallUnsuccessfulException | InvalidUnitsException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        String okunits = "imperial";
        try {
            Weather currentWeather = api.getCurrentWeather(coords, okunits);
            assertNotNull(currentWeather);
            assertEquals("Helsinki", currentWeather.getLocation());
        } catch (APICallUnsuccessfulException | InvalidUnitsException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        //Test bad coords
        Coord badCoords = new Coord(-400000, 345); // Example bad coordinates
        try {
            api.getCurrentWeather(badCoords, units);
            fail("Expected APICallUnsuccessfulException to be thrown");
        } catch (APICallUnsuccessfulException e) {
            // Good, an exception was expected
        } catch (InvalidUnitsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        String badUnits = "invalidUnit";
        try {
            api.getCurrentWeather(coords, badUnits);
            fail("Expected InvalidUnitsException to be thrown");
        } catch (InvalidUnitsException e) {
            // Good, an exception was expected
        } catch (APICallUnsuccessfulException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of getForecast method, of class API.
     */
    @Test
    public void testGetForecast() throws Exception {
        API api = new API();
        
        // Test good inputs
        Coord coords = new Coord(60.1695, 24.9354); // Example coordinates for Helsinki
        String units = "metric";
        try {
            HashMap<LocalDateTime, Weather> forecast = api.getForecast(coords, units);
            assertNotNull(forecast);
            assertFalse(forecast.isEmpty());
        } catch (APICallUnsuccessfulException | InvalidUnitsException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        String okunits = "imperial";
        try {
            HashMap<LocalDateTime, Weather> forecast = api.getForecast(coords, okunits);
            assertNotNull(forecast);
            assertFalse(forecast.isEmpty());
        } catch (APICallUnsuccessfulException | InvalidUnitsException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        //Test bad coords
        Coord badCoords = new Coord(-400000, 345); // Example bad coordinates
        try {
            api.getForecast(badCoords, units);
            fail("Expected APICallUnsuccessfulException to be thrown");
        } catch (APICallUnsuccessfulException e) {
            // Good, an exception was expected
        } catch (InvalidUnitsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        String badUnits = "invalidUnit";
        try {
            api.getForecast(coords, badUnits);
            fail("Expected InvalidUnitsException to be thrown");
        } catch (InvalidUnitsException e) {
            // Good, an exception was expected
        } catch (APICallUnsuccessfulException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    public void testGetCityName() {
        API api = new API();
        // Call the actual method
        try {
            Coord testCoord = new Coord(60.1695, 24.9354);
            String cityName = api.getCityName(testCoord);

            // Assert the result
            assertEquals("Helsinki", cityName);
        } catch (APICallUnsuccessfulException e) {
            fail("Unsuccessfull API call in get_city_name");
        }
    }
    
}



