package org.documents.documents.db.mapper;

import java.util.Map;

public interface PropertyMapper<T> {
    void map(T entity, Map<String, ?> row);
}
