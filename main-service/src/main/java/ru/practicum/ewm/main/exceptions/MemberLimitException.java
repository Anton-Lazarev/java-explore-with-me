package ru.practicum.ewm.main.exceptions;

public class MemberLimitException extends RuntimeException {
    public MemberLimitException(String message) {
        super(message);
    }
}
