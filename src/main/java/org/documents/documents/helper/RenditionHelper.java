package org.documents.documents.helper;

import org.documents.documents.db.entity.ContentEntity;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;


public interface RenditionHelper {

    Mono<ContentEntity> getRendition(ContentEntity entity, MediaType mediaType);

    Mono<ContentEntity> getOrRequestRendition(ContentEntity entity, MediaType mediaType);

    boolean isSupported(String sourceMimeType, String targetMimeType);

}
