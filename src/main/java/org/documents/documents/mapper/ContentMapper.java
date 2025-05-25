package org.documents.documents.mapper;

import org.documents.documents.entity.ContentEntity;
import org.documents.documents.model.rest.Content;

public interface ContentMapper {
    Content map(ContentEntity input);
}
