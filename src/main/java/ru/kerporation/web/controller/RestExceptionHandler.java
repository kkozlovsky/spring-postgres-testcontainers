package ru.kerporation.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kerporation.model.exception.ExceptionBody;
import ru.kerporation.model.exception.PostNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody postNotFound(final PostNotFoundException e) {
        return new ExceptionBody("Post not found.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody postNotValid(final MethodArgumentNotValidException e) {
        final StringBuilder builder = new StringBuilder();
        for (final ObjectError error : e.getAllErrors()) {
            builder.append(error.getDefaultMessage()).append(" ");
        }
        return new ExceptionBody(builder.toString());
    }

}