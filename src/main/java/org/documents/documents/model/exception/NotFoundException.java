package org.documents.documents.model.exception;

import java.util.UUID;

public class NotFoundException extends ApiException{
    public NotFoundException(Class<?> modelClass, UUID uuid) {
        super(ErrorCode.ENTITY_BY_UUID_NOT_FOUND, modelClass.getSimpleName(), uuid);
    }
}
