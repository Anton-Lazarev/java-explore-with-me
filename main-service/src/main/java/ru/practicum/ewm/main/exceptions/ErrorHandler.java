package ru.practicum.ewm.main.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.main.event.ConflictStatusException;
import ru.practicum.ewm.main.exceptions.dto.ErrorWithStackTraceDTO;
import ru.practicum.ewm.main.exceptions.dto.ShortErrorDTO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({IncorrectOwnerOfEventException.class, RequestAlreadyExistException.class, MemberLimitException.class,
            IncorrectRequesterException.class, ConflictStatusException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ShortErrorDTO handleConflictException(Exception exception) {
        log.error("Conflict in data error: {}", exception.getMessage());
        return ShortErrorDTO.builder()
                .status(HttpStatus.CONFLICT)
                .reason("For the requested operation the conditions are not met")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler({PageValidationException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
            IncorrectStatusException.class, EventDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ShortErrorDTO handleValidationException(Exception exception) {
        log.error("Illegal argument error: {}", exception.getMessage());
        return ShortErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Received incorrect data")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, EventNotFoundException.class,
            EventRequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ShortErrorDTO handleNotFoundException(Exception exception) {
        log.error("Data not found error: {}", exception.getMessage());
        return ShortErrorDTO.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorWithStackTraceDTO handleUnexpectedError(Throwable exception) {
        log.error("Unexpected error: {}", exception.getMessage());
        StringWriter out = new StringWriter();
        exception.printStackTrace(new PrintWriter(out));
        String stack = out.toString();
        return ErrorWithStackTraceDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Catch unexpected error")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .errors(Collections.singletonList(stack))
                .build();
    }
}
