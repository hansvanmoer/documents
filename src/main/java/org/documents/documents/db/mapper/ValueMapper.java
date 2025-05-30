package org.documents.documents.db.mapper;

public interface ValueMapper<T> {
    T convert(Object value);
}
