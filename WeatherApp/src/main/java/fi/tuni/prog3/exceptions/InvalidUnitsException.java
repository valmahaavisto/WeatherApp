/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.exceptions;

/**
 * Thrown when units are wrong in API call. Need to be metric or imperial.
 * @author Aarni Akkala
 */
public class InvalidUnitsException extends Exception {
    public InvalidUnitsException(String message) {
        super(message);
    }
}