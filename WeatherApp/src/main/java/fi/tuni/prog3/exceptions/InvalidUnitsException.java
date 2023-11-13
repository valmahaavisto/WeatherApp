/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.exceptions;

/**
 * Stores coordinates of a location latitude and longitude
 */


public class InvalidUnitsException extends Exception {
    public InvalidUnitsException(String message) {
        super("Available units: metric or imperial");
    }
}