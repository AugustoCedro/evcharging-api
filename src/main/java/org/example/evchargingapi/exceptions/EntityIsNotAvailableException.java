package org.example.evchargingapi.exceptions;

public class EntityIsNotAvailableException extends RuntimeException {
    public EntityIsNotAvailableException(String message) {
        super(message);
    }
}
