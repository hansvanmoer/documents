package org.documents.documents.service;

import reactor.core.publisher.Flux;

import java.util.UUID;

public interface SearchService {
    /**
     * Finds and indexes documents whose content
     * has not been indexed
     * @return a producer with the documents' UUID's
     */
    Flux<UUID> indexDocuments();
}
