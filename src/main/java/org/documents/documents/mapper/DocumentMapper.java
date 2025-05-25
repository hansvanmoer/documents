package org.documents.documents.mapper;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.model.api.Document;
import org.documents.documents.search.document.DocumentSearchDocument;

public interface DocumentMapper {

    Document map(DocumentEntity documentEntity, ContentEntity contentEntity);

    DocumentSearchDocument mapToSearchDocument(DocumentEntity documentEntity);

}
