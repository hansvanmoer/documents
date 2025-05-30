package org.documents.documents.service;

import org.documents.documents.model.DocumentUpdate;
import org.documents.documents.model.api.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DocumentService {
    Mono<Document> create(String title, UUID contentUuid);

    Mono<Document> get(UUID uuid);

    Mono<Page<Document>> list(Pageable pageable);

    Flux<Document> search(Pageable pageable, String term);

    Mono<Document> update(UUID uuid, DocumentUpdate update);

    Mono<Void> delete(UUID uuid);
}
