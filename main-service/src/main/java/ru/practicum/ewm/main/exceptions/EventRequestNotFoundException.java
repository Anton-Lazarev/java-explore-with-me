package ru.practicum.ewm.main.exceptions;

public class EventRequestNotFoundException extends RuntimeException {
    public EventRequestNotFoundException(String message) {
        super(message);
    }
}
