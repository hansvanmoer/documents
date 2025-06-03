package org.documents.documents.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR),
    ENTITY_BY_UUID_NOT_FOUND(HttpStatus.NOT_FOUND),
    FILE_COPY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_VALUE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_VALUE_CONVERSION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatusCode httpStatusCode;

    ErrorCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
