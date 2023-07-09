package ru.practicum.ewm.main.event;

public class ConflictStatusException extends RuntimeException {
    public ConflictStatusException(String message) {
        super(message);
    }
}
