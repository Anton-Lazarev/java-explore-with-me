package ru.practicum.ewm.main.exceptions;

public class LikeAlreadyExistException extends RuntimeException {
    public LikeAlreadyExistException(String message) {
        super(message);
    }
}
