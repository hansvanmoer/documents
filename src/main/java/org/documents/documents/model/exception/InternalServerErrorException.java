package org.documents.documents.model.exception;

public class InternalServerErrorException extends ApiException {
    public InternalServerErrorException(ErrorCode errorCode, Throwable cause, String message, Object... args) {
        super(errorCode, cause, message, args);
    }
}
