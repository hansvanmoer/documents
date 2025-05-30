package org.documents.documents.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.model.DocumentWithContentEntity;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.model.DocumentAndContentEntities;
import org.documents.documents.model.DocumentToIndex;
import org.documents.documents.model.api.Document;
import org.documents.documents.search.document.DocumentSearchDocument;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class DocumentMapperImpl implements DocumentMapper {

    private final TemporalHelper temporalHelper;

    @Override
    public Document map(DocumentAndContentEntities entities) {
        final DocumentEntity documentEntity = entities.documentEntity();
        final ContentEntity contentEntity = entities.contentEntity();
        return new Document(
                UUID.fromString(documentEntity.getUuid()),
                temporalHelper.fromDatabaseTime(documentEntity.getCreated()),
                temporalHelper.fromDatabaseTime(documentEntity.getModified()),
                temporalHelper.fromDatabaseTime(documentEntity.getContentModified()),
                UUID.fromString(contentEntity.getUuid()),
                contentEntity.getMimeType(),
                documentEntity.getTitle()
        );
    }

    @Override
    public Document map(DocumentWithContentEntity input) {
        return new Document(
                UUID.fromString(input.getUuid()),
                temporalHelper.fromDatabaseTime(input.getCreated()),
                temporalHelper.fromDatabaseTime(input.getModified()),
                temporalHelper.fromDatabaseTime(input.getContentModified()),
                UUID.fromString(input.getUuid()),
                input.getMimeType(),
                input.getTitle()
        );
    }

    @Override
    public Document map(DocumentSearchDocument input) {
        return new Document(
                UUID.fromString(input.getUuid()),
                input.getCreated(),
                input.getModified(),
                input.getContentModified(),
                UUID.fromString(input.getUuid()),
                input.getMimeType(),
                input.getTitle()
        );
    }

    @Override
    public DocumentSearchDocument mapToDocumentSearchDocument(DocumentToIndex input) {
        final DocumentEntity documentEntity = input.documentEntity();
        final ContentEntity contentEntity = input.contentEntity();
        final DocumentSearchDocument searchDocument = new DocumentSearchDocument();
        searchDocument.setId(documentEntity.getId());
        searchDocument.setUuid(documentEntity.getUuid());
        searchDocument.setMimeType(contentEntity.getMimeType());
        searchDocument.setTitle(documentEntity.getTitle());
        searchDocument.setCreated(temporalHelper.fromDatabaseTime(documentEntity.getCreated()));
        searchDocument.setModified(temporalHelper.fromDatabaseTime(documentEntity.getModified()));
        searchDocument.setContentModified(temporalHelper.fromDatabaseTime(documentEntity.getContentModified()));
        searchDocument.setTitle(documentEntity.getTitle());
        searchDocument.setContent(input.contentAsText());
        return searchDocument;
    }

}
