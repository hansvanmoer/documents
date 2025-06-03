package org.documents.documents.db.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.mapper.PropertyConversionException;
import org.documents.documents.db.mapper.PropertyMapper;
import org.documents.documents.db.mapper.ValueMapper;
import org.documents.documents.model.exception.ErrorCode;
import org.documents.documents.model.exception.InternalServerErrorException;

import java.util.Map;
import java.util.function.BiConsumer;

@AllArgsConstructor
public class PropertyMappingImpl<T, U> implements PropertyMapper<T> {
    private String name;
    private BiConsumer<T, U> setter;
    private ValueMapper<U> valueMapper;

    public void map(T entity, Map<String, ?> row) {
        final Object value = row.get(name);
        if(value == null) {
            throw new InternalServerErrorException(ErrorCode.DATABASE_VALUE_NOT_FOUND, name);
        }
        try {
            setter.accept(entity, valueMapper.convert(value));
        } catch(PropertyConversionException e) {
            throw new InternalServerErrorException(ErrorCode.DATABASE_VALUE_CONVERSION_FAILED, name);
        }
    }
}
