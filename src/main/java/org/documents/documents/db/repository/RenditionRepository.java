package org.documents.documents.db.repository;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.RenditionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RenditionRepository extends ReactiveCrudRepository<RenditionEntity, Long> {

    @Query("SELECT r FROM content o, rendition r, content t WHERE r.originalId = o.id AND o.id = ?1 AND t.mimeType = ?2")
    Mono<ContentEntity> getRenditionForMimeType(long originalId, String mimeType);
}
