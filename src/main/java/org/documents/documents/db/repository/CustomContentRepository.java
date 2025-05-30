package org.documents.documents.db.repository;

import org.documents.documents.db.entity.ContentEntity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface CustomContentRepository {

    Flux<ContentEntity> findAll(Pageable pageable);

}
