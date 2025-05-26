package org.documents.documents.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.model.DocumentWithContentEntity;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.model.api.Document;
import org.documents.documents.search.document.DocumentSearchDocument;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class DocumentMapperImpl implements DocumentMapper {

    private final TemporalHelper temporalHelper;

    @Override
    public Document map(DocumentEntity documentEntity, ContentEntity contentEntity) {
        return new Document(
                UUID.fromString(documentEntity.getUuid()),
                temporalHelper.fromDatabaseTime(documentEntity.getCreated()),
                contentEntity.getMimeType(),
                documentEntity.getContentIndexStatus(),
                documentEntity.getTitle()
        );
    }

    @Override
    public Document map(DocumentWithContentEntity input) {
        return new Document(
                UUID.fromString(input.getUuid()),
                temporalHelper.fromDatabaseTime(input.getCreated()),
                input.getMimeType(),
                input.getContentIndexStatus(),
                input.getTitle()
        );
    }

    @Override
    public DocumentSearchDocument mapToSearchDocument(DocumentEntity documentEntity, String text) {
        final DocumentSearchDocument searchDocument = new DocumentSearchDocument();
        searchDocument.setId(documentEntity.getId());
        searchDocument.setUuid(documentEntity.getUuid());
        searchDocument.setTitle(documentEntity.getTitle());
        searchDocument.setCreated(documentEntity.getCreated());
        searchDocument.setTitle(documentEntity.getTitle());
        searchDocument.setContent(text);
        return searchDocument;
    }
}
