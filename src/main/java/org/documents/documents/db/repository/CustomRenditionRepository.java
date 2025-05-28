package org.documents.documents.db.repository;

import org.documents.documents.db.entity.RenditionEntity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface CustomRenditionRepository {
    Flux<RenditionEntity> findAll(Pageable pageable);
}
