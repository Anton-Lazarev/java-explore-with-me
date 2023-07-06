package ru.practicum.ewm.main.exceptions;

public class PageValidationException extends RuntimeException {
    public PageValidationException(String message) {
        super(message);
    }
}
