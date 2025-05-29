package org.documents.documents.model;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;

import java.util.Objects;

public record DocumentAndContentEntities(DocumentEntity documentEntity, ContentEntity contentEntity) {
    public boolean contentHasChanged() {
        return !Objects.equals(documentEntity.getContentId(), contentEntity.getId());
    }

    public DocumentAndContentEntities withDocumentEntity(DocumentEntity updatedDocumentEntity) {
        return new DocumentAndContentEntities(updatedDocumentEntity, contentEntity);
    }
    public DocumentToIndex toDocumentToIndex() {
        return new DocumentToIndex(documentEntity, contentEntity, "");
    }
}
