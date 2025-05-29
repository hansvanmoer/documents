package org.documents.documents.search.repository;

import reactor.core.publisher.Mono;

public interface CustomDocumentSearchRepository {

    Mono<Void> updateDocumentMetadata(Long id, String title);

}
