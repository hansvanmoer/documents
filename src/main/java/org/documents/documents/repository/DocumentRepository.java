package org.documents.documents.repository;

import org.documents.documents.entity.DocumentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DocumentRepository extends ReactiveCrudRepository<DocumentEntity, Long> {
}