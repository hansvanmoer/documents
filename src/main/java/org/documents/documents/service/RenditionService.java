package org.documents.documents.service;

import org.documents.documents.model.api.Rendition;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RenditionService {

    Mono<Rendition> getOrRequest(UUID documentUuid, String mimeType);

    Flux<Rendition> list(Pageable pageable);

    Flux<Rendition> listByDocumentUuid(UUID documentUuid);

    Flux<Rendition> listByMimeType(String mimeType, Pageable pageable);

    Mono<Rendition> get(UUID renditionUuid);

    Mono<Void> delete(UUID rendtionUuid);
}
