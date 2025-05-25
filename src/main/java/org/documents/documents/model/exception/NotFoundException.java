package org.documents.documents.model.exception;

public class NotFoundException extends ApiException{
    public NotFoundException(String message, Object... args) {
        super(ErrorCode.ENTITY_NOT_FOUND, message, args);
    }
}
