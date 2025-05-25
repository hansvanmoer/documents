package org.documents.documents.search.repository;

import org.documents.documents.search.document.DocumentSearchDocument;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

public interface DocumentSearchRepository extends ReactiveElasticsearchRepository<DocumentSearchDocument, String> {

}
