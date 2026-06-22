/*
* This class represents a custom exception for resource not found scenarios.
* It extends RuntimeException and is used to indicate that a requested resource could not be found.
*/
package com.example.inventory.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
