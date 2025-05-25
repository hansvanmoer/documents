package org.documents.documents.db.repository;

import org.documents.documents.db.entity.ContentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ContentRepository extends ReactiveCrudRepository<ContentEntity, Long> {

    Mono<ContentEntity> findByUuid(String uuid);
}
