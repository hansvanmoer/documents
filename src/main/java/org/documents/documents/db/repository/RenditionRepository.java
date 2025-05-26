package org.documents.documents.db.repository;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.RenditionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RenditionRepository extends ReactiveCrudRepository<RenditionEntity, Long> {

    Mono<RenditionEntity> findByContentIdAndMimeType(long contentId, String mimeType);
}
