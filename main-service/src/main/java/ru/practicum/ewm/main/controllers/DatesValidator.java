package ru.practicum.ewm.main.controllers;

import ru.practicum.ewm.main.exceptions.EventDateException;

import java.time.LocalDateTime;

public class DatesValidator {
    public static void validate(LocalDateTime start, LocalDateTime end) {
        if(start.isAfter(end)) {
            throw new EventDateException("Start date should be after end date");
        }
    }
}
