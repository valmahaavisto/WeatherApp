/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

/**
 * Represents geographical coordinates with latitude and longitude.
 * This class provides methods to get and set latitude and longitude values.
 */
public class Coord {
    
    /**
     * Latitude of the geographical coordinates.
     */
    private double lat;
    
    /**
     * Longitude of the geographical coordinates.
     */
    private double lon;

    /**
     * Constructs a Coord object with the specified latitude and longitude.
     *
     * @param lat Latitude value.
     * @param lon Longitude value.
     */
    public Coord(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Retrieves the latitude value of the coordinates.
     *
     * @return Latitude value.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Sets the latitude value of the coordinates.
     *
     * @param lat New latitude value to set.
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Retrieves the longitude value of the coordinates.
     *
     * @return Longitude value.
     */
    public double getLon() {
        return lon;
    }

    /**
     * Sets the longitude value of the coordinates.
     *
     * @param lon New longitude value to set.
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
