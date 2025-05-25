package org.documents.documents.service;

import org.documents.documents.model.rest.Content;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContentService {

    Mono<Content> upload(MediaType mimeType, Flux<DataBuffer> content);

}
