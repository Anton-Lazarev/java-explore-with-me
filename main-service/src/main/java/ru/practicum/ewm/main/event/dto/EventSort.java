package ru.practicum.ewm.main.event.dto;

import java.util.Optional;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    public static Optional<EventSort> from(String text) {
        for (EventSort status : values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
