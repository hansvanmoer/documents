package org.documents.documents.service;

import org.documents.documents.model.api.Content;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ContentService {

    Mono<Content> upload(MediaType mimeType, Flux<DataBuffer> content);

    Mono<Content> get(UUID uuid);

    Mono<Page<Content>> list(Pageable pageable);

    Mono<Void> download(ServerHttpResponse response, UUID uuid);
}
