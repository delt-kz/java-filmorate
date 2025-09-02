package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMissingPathVariable(final MissingPathVariableException e) {
        log.warn("Не указан параметр пути: " + e.getVariableName());
        return new ResponseEntity<>(new ErrorResponse("Не указан параметр пути: " + e.getVariableName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleThrowable(final Throwable e) {
        log.warn("Неожиданная ошибка ", e);
        return new ResponseEntity<>(new ErrorResponse("Внутренняя ошибка сервера"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        String error = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("Ошибка валидаций: {}", error);
        return new ResponseEntity<>(new ErrorResponse("Ошибка валидаций: " + error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgument(final RuntimeException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
