package org.documents.documents.helper;

import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.model.DocumentAndContentEntities;
import reactor.core.publisher.Mono;

public interface IndexHelper {

    Mono<DocumentAndContentEntities> indexDocument(DocumentAndContentEntities entities);

    Mono<DocumentAndContentEntities> indexDocumentIfRenditionExists(DocumentEntity documentEntity);

    Mono<DocumentAndContentEntities> indexDocumentIfRenditionExists(DocumentAndContentEntities entities);

    Mono<DocumentAndContentEntities> reindexDocument(DocumentAndContentEntities entities);

    Mono<DocumentAndContentEntities> reindexDocumentMetadata(DocumentAndContentEntities entities);

    boolean canIndex(String mimeType);
}