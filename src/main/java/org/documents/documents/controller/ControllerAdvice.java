package org.documents.documents.controller;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.ApiSettings;
import org.documents.documents.model.api.ErrorResponse;
import org.documents.documents.model.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@RestControllerAdvice
public class ControllerAdvice {

    private final ApiSettings apiSettings;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        return e.toResponseEntity(apiSettings.getSupportedLocales());
    }
}
