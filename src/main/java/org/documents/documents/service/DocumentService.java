package org.documents.documents.service;

import org.documents.documents.model.api.Document;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DocumentService {
    Mono<Document> create(UUID contentUuid);
}
