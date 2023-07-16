package ru.practicum.ewm.main.exceptions;

public class IncorrectOwnerOfEventException extends RuntimeException {
    public IncorrectOwnerOfEventException(String message) {
        super(message);
    }
}
