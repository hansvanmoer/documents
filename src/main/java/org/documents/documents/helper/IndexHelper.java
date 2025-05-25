package org.documents.documents.helper;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import reactor.core.publisher.Mono;

public interface IndexHelper {

    Mono<DocumentEntity> indexDocument(DocumentEntity document);

    Mono<DocumentEntity> indexDocumentOrScheduleIndexation(DocumentEntity document, ContentEntity content);

}