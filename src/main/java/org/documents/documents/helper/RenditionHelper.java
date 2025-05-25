package org.documents.documents.helper;

import org.documents.documents.entity.ContentEntity;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;


public interface RenditionHelper {

    Mono<ContentEntity> getOrRequestRendition(ContentEntity entity, MediaType mediaType);

}
