package org.documents.documents.model.exception;

public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;

    public ApiException(ErrorCode errorCategory, String message) {
        super(message);
        this.errorCode = errorCategory;
        this.args = null;
    }

    public ApiException(ErrorCode errorCategory, String message, Object ... args) {
        super(message);
        this.errorCode = errorCategory;
        this.args = args;
    }

    public ApiException(ErrorCode errorCode, Throwable cause, String message) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = null;
    }

    public ApiException(ErrorCode ErrorCode, Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
        this.errorCode = ErrorCode;
        this.args = args;
    }
}
