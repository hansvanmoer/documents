package org.documents.documents.repository;

import org.documents.documents.entity.ContentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ContentRepository extends ReactiveCrudRepository<ContentEntity, Long> {

    Mono<ContentEntity> findByUuid(String uuid);
}
