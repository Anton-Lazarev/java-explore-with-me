package ru.practicum.ewm.main.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({PageValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationException(RuntimeException exception) {
        log.error("Error: {}", exception.getMessage());
        StringWriter out = new StringWriter();
        exception.printStackTrace(new PrintWriter(out));
        String stack = out.toString();
        return ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Received incorrect data")
                .message(exception.getMessage())
                .errors(Collections.singletonList(stack))
                .build();
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(RuntimeException exception) {
        log.error("Error: {}", exception.getMessage());
        StringWriter out = new StringWriter();
        exception.printStackTrace(new PrintWriter(out));
        String stack = out.toString();
        return ErrorDTO.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found")
                .message(exception.getMessage())
                .errors(Collections.singletonList(stack))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleUnexpectedError(Throwable exception) {
        log.error("Error: {}", exception.getMessage());
        StringWriter out = new StringWriter();
        exception.printStackTrace(new PrintWriter(out));
        String stack = out.toString();
        return ErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Catch unexpected error")
                .message(exception.getMessage())
                .errors(Collections.singletonList(stack))
                .build();
    }
}
