package org.documents.documents.model.exception;

public class InternalServerErrorException extends ApiException {
    public InternalServerErrorException(ErrorCode errorCode, Throwable cause, String message, Object... args) {
        super(errorCode, cause, message, args);
    }

    public InternalServerErrorException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
