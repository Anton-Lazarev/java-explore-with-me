package ru.practicum.ewm.main.event.dto;

import java.util.Optional;

public enum EventStatus {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<EventStatus> from(String text) {
        for (EventStatus status : values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
