package ru.sber.bookingservice.exceptions;

public class ResourceUnavailableException extends Exception {
    public ResourceUnavailableException(String message) {
        super(message);
    }
}
