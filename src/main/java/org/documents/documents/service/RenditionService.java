package org.documents.documents.service;

import org.documents.documents.model.api.Rendition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RenditionService {

    Mono<Rendition> getOrRequest(UUID documentUuid, String mimeType);

    Mono<Page<Rendition>> list(Pageable pageable);

    Flux<Rendition> listByDocumentUuid(UUID documentUuid);

    Mono<Page<Rendition>> listByMimeType(String mimeType, Pageable pageable);

    Mono<Rendition> get(UUID renditionUuid);

    Mono<Void> download(ServerHttpResponse response, UUID uuid);

    Mono<Void> delete(UUID rendtionUuid);
}
