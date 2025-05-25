package org.documents.documents.helper;

import org.documents.documents.db.entity.ContentEntity;
import reactor.core.publisher.Mono;

public interface ContentHelper {

    Mono<String> readContentAsString(ContentEntity entity);

}
