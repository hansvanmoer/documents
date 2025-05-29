package org.documents.documents.mapper;

import org.documents.documents.db.model.DocumentWithContentEntity;
import org.documents.documents.model.DocumentAndContentEntities;
import org.documents.documents.model.DocumentToIndex;
import org.documents.documents.model.api.Document;
import org.documents.documents.search.document.DocumentSearchDocument;

public interface DocumentMapper {

    Document map(DocumentAndContentEntities entities);

    Document map(DocumentWithContentEntity documentWithContentEntity);

    Document map(DocumentSearchDocument document);

    DocumentSearchDocument mapToDocumentSearchDocument(DocumentToIndex documentWithContent);

}
