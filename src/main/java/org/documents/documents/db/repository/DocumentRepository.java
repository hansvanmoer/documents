package org.documents.documents.db.repository;

import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.entity.ContentIndexStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DocumentRepository extends ReactiveCrudRepository<DocumentEntity, Long> {

    Mono<DocumentEntity> findByUuid(String uuid);

    Flux<DocumentEntity> findByContentIndexStatus(ContentIndexStatus contentIndexStatus);
}