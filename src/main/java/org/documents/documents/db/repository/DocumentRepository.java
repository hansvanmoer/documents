package org.documents.documents.db.repository;

import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.model.api.ContentIndexStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DocumentRepository extends ReactiveCrudRepository<DocumentEntity, Long> {
    Flux<DocumentEntity> findByContentIndexStatus(ContentIndexStatus contentIndexStatus);
}