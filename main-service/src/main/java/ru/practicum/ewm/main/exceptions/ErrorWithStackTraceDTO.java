package ru.practicum.ewm.main.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ErrorWithStackTraceDTO {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final String timestamp;
    private final List<String> errors;
}
