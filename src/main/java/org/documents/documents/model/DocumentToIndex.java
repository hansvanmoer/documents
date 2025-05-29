package org.documents.documents.model;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;

public record DocumentToIndex(DocumentEntity documentEntity, ContentEntity contentEntity, String contentAsText) {

    public DocumentToIndex withDocumentEntity(DocumentEntity updatedDocumentEntity) {
        return new DocumentToIndex(updatedDocumentEntity, contentEntity, contentAsText);
    }

    public DocumentToIndex withContentAsText(String updatedContentAsText) {
        return new DocumentToIndex(documentEntity, contentEntity, updatedContentAsText);
    }

    public DocumentAndContentEntities toDocumentAndContentEntities() {
        return new DocumentAndContentEntities(documentEntity, contentEntity);
    }
}
