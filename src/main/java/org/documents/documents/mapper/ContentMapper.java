package org.documents.documents.mapper;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.model.api.Content;

public interface ContentMapper {
    Content map(ContentEntity input);
}
