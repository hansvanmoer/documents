package org.documents.documents.db.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.mapper.PropertyConversionException;
import org.documents.documents.db.mapper.ValueMapper;

import java.util.function.Function;

@AllArgsConstructor
public class StringValueMapper<T> implements ValueMapper<T> {
    private final Function<String, T> converter;

    @Override
    public T convert(Object value) throws PropertyConversionException {
        try {
            return converter.apply(value.toString());
        } catch(IllegalArgumentException e) {
            throw new PropertyConversionException(e);
        }
    }
}