package org.documents.documents.db.repository;

import org.documents.documents.db.entity.RenditionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RenditionRepository extends ReactiveCrudRepository<RenditionEntity, Long> {

    Mono<RenditionEntity> findByContentIdAndMimeType(long contentId, String mimeType);

    Flux<RenditionEntity> findByContentId(long contentId);

    Flux<RenditionEntity> findByMimeType(String mimeType, Pageable pageable);

    Mono<RenditionEntity> findByUuid(String uuid);

    Mono<Void> deleteByUuid(String uuid);
}
