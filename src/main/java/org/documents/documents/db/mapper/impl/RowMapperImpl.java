package org.documents.documents.db.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.mapper.RowMapper;
import org.documents.documents.db.mapper.ValueMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class RowMapperImpl<T> implements RowMapper<T> {

    private final Supplier<T> constructor;
    private final List<PropertyMappingImpl<T, ?>> propertyMappings;

    private RowMapperImpl(Supplier<T> constructor, List<PropertyMappingImpl<T, ?>> propertyMappings) {
        this.constructor = constructor;
        this.propertyMappings = Collections.unmodifiableList(propertyMappings);
    }

    @Override
    public T map(Map<String, Object> row) {
        final T entity = constructor.get();
        propertyMappings.forEach(propertyMapping -> propertyMapping.map(entity, row));
        return entity;
    }

    public static <T> Builder<T> builder(Supplier<T> constructor) {
        return new Builder<>(constructor, new ArrayList<>());
    }

    @AllArgsConstructor
    public static class Builder<T> {
        private final Supplier<T> constructor;
        private final List<PropertyMappingImpl<T, ?>> propertyMappings;

        public <U> Builder<T> property(String name, BiConsumer<T, U> setter) {
            return property(name, setter, new PlainValueMapper<>());
        }

        public <U> Builder<T> property(String name, BiConsumer<T, U> setter, Function<String, U> converter) {
            return property(name, setter, new StringValueMapper<>(converter));
        }

        private <U> Builder<T> property(String name, BiConsumer<T, U> setter, ValueMapper<U> mapper) {
            this.propertyMappings.add(new PropertyMappingImpl<>(name, setter, mapper));
            return this;
        }

        public RowMapperImpl<T> build() {
            return new RowMapperImpl<>(constructor, propertyMappings);
        }
    }
}
