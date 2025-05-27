package org.documents.documents.search.repository;

import org.documents.documents.search.document.DocumentSearchDocument;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface DocumentSearchRepository extends ReactiveElasticsearchRepository<DocumentSearchDocument, Long> {
    Flux<DocumentSearchDocument> findByContent(String content);
}
