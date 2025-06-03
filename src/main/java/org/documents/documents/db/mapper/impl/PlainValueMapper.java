package org.documents.documents.db.mapper.impl;

import org.documents.documents.db.mapper.PropertyConversionException;
import org.documents.documents.db.mapper.ValueMapper;

public class PlainValueMapper<T> implements ValueMapper<T> {

    @Override
    public T convert(Object value) throws PropertyConversionException {
        try {
            @SuppressWarnings("unchecked") final T converted =  (T)value;
            return converted;
        } catch(ClassCastException e) {
            throw new PropertyConversionException(e);
        }
    }
}
