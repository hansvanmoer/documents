package org.documents.documents.db.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.mapper.ValueMapper;
import org.documents.documents.model.exception.ErrorCode;
import org.documents.documents.model.exception.InternalServerErrorException;

import java.util.function.Function;

@AllArgsConstructor
public class StringValueMapper<T> implements ValueMapper<T> {
    private final Function<String, T> converter;

    @Override
    public T convert(Object value) {
        try {
            return converter.apply(value.toString());
        } catch(IllegalArgumentException e) {
            throw new InternalServerErrorException(ErrorCode.DATABASE_MAPPING_FAILED, e, "could not convert property value");
        }
    }
}