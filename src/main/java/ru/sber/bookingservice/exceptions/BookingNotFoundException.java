package ru.sber.bookingservice.exceptions;

public class BookingNotFoundException extends Exception {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
