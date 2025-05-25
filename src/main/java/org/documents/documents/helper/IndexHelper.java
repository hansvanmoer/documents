package org.documents.documents.helper;

import org.documents.documents.entity.ContentEntity;
import org.documents.documents.entity.DocumentEntity;
import reactor.core.publisher.Mono;

public interface IndexHelper {

    Mono<Void> indexDocument(DocumentEntity document, ContentEntity content);

}