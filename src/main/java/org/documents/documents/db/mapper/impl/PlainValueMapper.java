package org.documents.documents.db.mapper.impl;

import org.documents.documents.db.mapper.ValueMapper;
import org.documents.documents.model.exception.ErrorCode;
import org.documents.documents.model.exception.InternalServerErrorException;

public class PlainValueMapper<T> implements ValueMapper<T> {

    @Override
    public T convert(Object value) {
        try {
            @SuppressWarnings("unchecked") final T converted =  (T)value;
            return converted;
        } catch(ClassCastException e) {
            throw new InternalServerErrorException(ErrorCode.DATABASE_MAPPING_FAILED, e, "could not convert property value");
        }
    }
}
