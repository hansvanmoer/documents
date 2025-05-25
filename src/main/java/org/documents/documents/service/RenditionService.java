package org.documents.documents.service;

import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RenditionService {

    Mono<UUID> requestRendition(UUID contentUuid, MediaType mediaType);
}
