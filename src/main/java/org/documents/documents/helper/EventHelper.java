package org.documents.documents.helper;

import org.documents.documents.model.api.Content;
import org.documents.documents.model.api.Document;
import org.documents.documents.model.api.Rendition;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EventHelper {

    Mono<Void> notifyContentCreated(Content content);

    Mono<Void> notifyRenditionCreated(Rendition rendition);

    Mono<Void> notifyRenditionDeleted(UUID renditionUuid);

    Mono<Void> notifyDocumentCreated(Document document);

    Mono<Void> notifyDocumentUpdated(Document document);

    Mono<Void> notifyDocumentDeleted(UUID documentUuid);
}
