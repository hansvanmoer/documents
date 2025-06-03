package org.documents.documents.model.exception;

public class InternalServerErrorException extends ApiException {
    public InternalServerErrorException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }

    public InternalServerErrorException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
