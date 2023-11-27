/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Aarni Akkala
 */
public class LocationWeatherTest {

    @Test
    public void testSetCurrentWeather() {
        
        Coord coords = new Coord(23,23);
        
        // Create a Weather instance for testing
        Weather testWeather = new Weather(
                LocalDateTime.of(2023, 11, 17, 17, 44),
                "Tampere",
                "FI",
                coords,
                10.0,
                20.0,
                15.0,
                14.0,
                5.0,
                180.0,
                7.0,
                10.0,
                800,
                "broken clouds",
                0.0
        );

        // Create a LocationWeather instance for testing
        LocationWeather locationWeather = new LocationWeather(new HashMap<>(), null);

        // Set the current weather
        locationWeather.setCurrentWeather(testWeather);

        // Check if the current weather was set correctly
        assertEquals(testWeather, locationWeather.getCurrentWeather());
    }

    @Test
    public void testGetDays() {
        
        Coord coords = new Coord(23,23);
        // Create a Weather instance for testing
        Weather testWeather1 = new Weather(
                LocalDateTime.of(2023, 11, 17, 17, 44),
                "Tampere",
                "FI",
                coords,
                10.0,
                20.0,
                15.0,
                14.0,
                5.0,
                180.0,
                7.0,
                10.0,
                800,
                "broken clouds",
                0.0
        );

        // Create another Weather instance for testing with a different date
        Weather testWeather2 = new Weather(
                LocalDateTime.of(2023, 11, 18, 12, 0),
                "Tampere",
                "FI",
                coords,
                8.0,
                18.0,
                12.0,
                10.0,
                6.0,
                200.0,
                8.0,
                5.0,
                801,
                "few clouds",
                0.0
        );

        // Create a LocationWeather instance for testing
        LocationWeather locationWeather = new LocationWeather(new HashMap<>(), null);

        // Add the test weathers to the forecast
        locationWeather.getForecast().put(testWeather1.getDate(), testWeather1);
        locationWeather.getForecast().put(testWeather2.getDate(), testWeather2);

        // Check if the getDays method returns the correct list of days
        assertEquals(2, locationWeather.getDays().size());
        assertTrue(locationWeather.getDays().contains(testWeather1.getDate().toLocalDate().atStartOfDay()));
        assertTrue(locationWeather.getDays().contains(testWeather2.getDate().toLocalDate().atStartOfDay()));
    }
}
