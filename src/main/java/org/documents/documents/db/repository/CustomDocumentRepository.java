package org.documents.documents.db.repository;

import org.documents.documents.db.model.DocumentWithContentEntity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface CustomDocumentRepository {
    Flux<DocumentWithContentEntity> find(Pageable pageable);
}
