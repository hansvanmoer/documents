package org.documents.documents.service;

import org.documents.documents.model.api.Rendition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RenditionService {
    Flux<Rendition> getDocumentRenditions(UUID documentUuid);

    Mono<Rendition> getOrRequestRendition(UUID documentUuid, String mimeType);
}
