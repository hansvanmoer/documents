package org.documents.documents.db.repository;

import org.documents.documents.db.model.DocumentWithContentEntity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomDocumentRepository {

    Mono<DocumentWithContentEntity> findByUuid(UUID uuid);

    Flux<DocumentWithContentEntity> find(Pageable pageable);
}
