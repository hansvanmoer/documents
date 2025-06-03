package org.documents.documents.controller;

import org.documents.documents.model.api.ErrorResponse;
import org.documents.documents.model.exception.ApiException;
import org.documents.documents.model.exception.InternalServerErrorException;
import org.documents.documents.model.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApiException(NotFoundException e) {
        return createResponseEntity(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleApiException(InternalServerErrorException e) {
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(HttpStatusCode code, ApiException e) {
        return ResponseEntity.status(code)
                .body(new ErrorResponse(
                        e.getErrorCode(),
                        e.getMessage(),
                        e.getArgs() == null ? Collections.emptyList() : Stream.of(e.getArgs()).map(Objects::toString).toList())
                );
    }
}
