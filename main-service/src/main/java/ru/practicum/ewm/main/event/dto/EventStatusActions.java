package ru.practicum.ewm.main.event.dto;

import java.util.Optional;

public enum EventStatusActions {
    SEND_TO_REVIEW,
    CANCEL_REVIEW,
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static Optional<EventStatusActions> from(String text) {
        for (EventStatusActions status : values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return java.util.Optional.of(status);
            }
        }
        return java.util.Optional.empty();
    }
}
