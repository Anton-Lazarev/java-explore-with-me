package ru.practicum.ewm.main.exceptions;

public class IncorrectRequesterException extends RuntimeException {
    public IncorrectRequesterException(String message) {
        super(message);
    }
}
