package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.db.entity.ContentIndexStatus;
import org.documents.documents.service.SearchService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@AllArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {

    private final DocumentRepository documentRepository;
    private final IndexHelper indexHelper;

    @Override
    public Flux<UUID> indexWaitingDocumentsWithTextRenditions() {
        return documentRepository.findByContentIndexStatus(ContentIndexStatus.WAITING)
                .flatMap(indexHelper::indexDocumentIfRenditionExists)
                .map(entities -> UUID.fromString(entities.documentEntity().getUuid()));
    }
}
