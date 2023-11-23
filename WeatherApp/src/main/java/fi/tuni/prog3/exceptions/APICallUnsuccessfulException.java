/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.exceptions;

/**
 * Thrown when API call is unsuccessfull for any reason or data is not what
 * it should be.
 * @author Aarni Akkala
 */
public class APICallUnsuccessfulException extends Exception{
    public APICallUnsuccessfulException(String message) {
        super(message);
    }
}
