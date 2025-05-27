package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.model.api.ContentIndexStatus;
import org.documents.documents.model.exception.NotFoundException;
import org.documents.documents.model.api.Document;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.search.document.DocumentSearchDocument;
import org.documents.documents.search.repository.DocumentSearchRepository;
import org.documents.documents.service.DocumentService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private final ContentRepository contentRepository;
    private final CustomDocumentRepository customDocumentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final DocumentSearchRepository documentSearchRepository;
    private final IndexHelper indexHelper;
    private final RenditionHelper renditionHelper;
    private final TemporalHelper temporalHelper;
    private final UuidHelper uuidHelper;

    @Override
    public Mono<Document> create(String title, UUID contentUuid) {
        return contentRepository.findByUuid(contentUuid.toString())
            .switchIfEmpty(Mono.error(new NotFoundException("content for new document not found", contentUuid)))
            .flatMap(content -> create(title, content));
    }

    @Override
    public Mono<Document> get(UUID uuid) {
        return customDocumentRepository.findByUuid(uuid).map(documentMapper::map);
    }

    @Override
    public Flux<Document> list(Pageable pageable) {
        return customDocumentRepository.find(pageable).map(documentMapper::map);
    }

    @Override
    public Flux<Document> search(Pageable pageable, String term) {
        return documentSearchRepository.findByContent(term)
                .map(DocumentSearchDocument::getId)
                .flatMap(documentRepository::findById)
                .flatMap(documentEntity -> contentRepository.findById(documentEntity.getContentId()).map(c -> documentMapper.map(documentEntity, c)));
    }

    @Override
    public Mono<Void> delete(UUID uuid) {
        return documentRepository.findByUuid(uuid.toString()).flatMap(documentRepository::delete);
    }

    private Mono<Document> create(String title, ContentEntity contentEntity) {
        final ZonedDateTime now = temporalHelper.now();
        final UUID uuid = uuidHelper.createUuid();
        final DocumentEntity entity = new DocumentEntity();
        entity.setUuid(uuid.toString());
        entity.setCreated(temporalHelper.toDatabaseTime(now));
        entity.setContentId(contentEntity.getId());
        entity.setContentIndexStatus(isIndexSupported(contentEntity) ? ContentIndexStatus.WAITING : ContentIndexStatus.UNSUPPORTED);
        entity.setTitle(title);
        return documentRepository.save(entity)
                .flatMap(d -> indexHelper.indexDocument(d, contentEntity))
                .map(d -> documentMapper.map(d, contentEntity));
    }

    private boolean isIndexSupported(ContentEntity contentEntity) {
        return renditionHelper.isSupported(contentEntity.getMimeType(), MediaType.TEXT_PLAIN_VALUE);
    }
}
