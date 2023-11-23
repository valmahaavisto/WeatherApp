/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import java.time.LocalDateTime;
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
public class WeatherTest {
    
    public WeatherTest() {
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

    @Test
    public void testWeather() {
        // Create a LocalDateTime instance for testing
        LocalDateTime testDate = LocalDateTime.of(2023, 11, 17, 17, 44);

        // Create a Weather instance for testing
        Weather testWeather = new Weather(
                testDate,
                "Tampere",
                "FI",
                10.0, // temp_min
                20.0, // temp_max
                15.0, // current_temp
                14.0, // feels_like
                5.0,  // wind_speed
                180.0, // wind_direction
                7.0,  // wind_gust
                10.0, // rain
                800,  // weather_id
                "broken clouds"
        );

        // Test getter methods
        assertEquals(testDate, testWeather.getDate());
        assertEquals("Tampere", testWeather.getLocation());
        assertEquals("FI", testWeather.getCountry());
        assertEquals(10.0, testWeather.getTemp_min(), 0.01); // Using delta for double comparison
        assertEquals(20.0, testWeather.getTemp_max(), 0.01);
        assertEquals(15.0, testWeather.getCurrent_temp(), 0.01);
        assertEquals(14.0, testWeather.getFeels_like(), 0.01);
        assertEquals(5.0, testWeather.getWind_speed(), 0.01);
        assertEquals(180.0, testWeather.getWind_direction(), 0.01);
        assertEquals(7.0, testWeather.getWind_gust(), 0.01);
        assertEquals(10.0, testWeather.getRain(), 0.01);
        assertEquals(800, testWeather.getWeather_id());
        assertEquals("broken clouds", testWeather.getDescription());
    }
    
}
