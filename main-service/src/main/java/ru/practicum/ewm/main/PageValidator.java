package ru.practicum.ewm.main;

import ru.practicum.ewm.main.exceptions.PageValidationException;

public class PageValidator {
    public static void validate(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new PageValidationException("Page or size can't be negative");
        }
    }
}
