package org.documents.documents.helper;

import org.documents.documents.entity.ContentEntity;
import reactor.core.publisher.Mono;

public interface ContentHelper {

    Mono<String> readContentAsString(ContentEntity entity);

}
