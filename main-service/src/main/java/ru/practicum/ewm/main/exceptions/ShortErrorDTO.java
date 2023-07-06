package ru.practicum.ewm.main.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ShortErrorDTO {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final String timestamp;
}
