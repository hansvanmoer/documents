package org.documents.documents.db.mapper;

import java.util.Map;

public interface RowMapper<T> {
    T map(Map<String, Object> row);
}
