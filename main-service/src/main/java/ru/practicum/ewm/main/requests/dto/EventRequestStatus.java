package ru.practicum.ewm.main.requests.dto;

import java.util.Optional;

public enum EventRequestStatus {
    PENDING,
    CANCELED,
    REJECTED,
    CONFIRMED;

    public static Optional<EventRequestStatus> from(String text) {
        for (EventRequestStatus status : values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
