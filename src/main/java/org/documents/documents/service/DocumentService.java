package org.documents.documents.service;

import org.documents.documents.model.api.Document;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DocumentService {
    Mono<Document> create(String title, UUID contentUuid);

    Mono<Document> get(UUID uuid);

    Flux<Document> list(Pageable pageable);

    Mono<Void> delete(UUID uuid);
}
