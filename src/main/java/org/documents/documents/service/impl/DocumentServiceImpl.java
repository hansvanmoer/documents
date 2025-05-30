package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.db.entity.ContentIndexStatus;
import org.documents.documents.model.DocumentAndContentEntities;
import org.documents.documents.model.DocumentUpdate;
import org.documents.documents.model.exception.NotFoundException;
import org.documents.documents.model.api.Document;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.search.repository.DocumentSearchRepository;
import org.documents.documents.service.DocumentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
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
                .map(documentMapper::map);
    }

    @Override
    public Mono<Document> update(UUID uuid, DocumentUpdate update) {
        return documentRepository.findByUuid(uuid.toString())
                .flatMap(documentEntity ->
                        Mono.justOrEmpty(update.getContentUuid().toString())
                                .flatMap(contentRepository::findByUuid)
                                .switchIfEmpty(contentRepository.findById(documentEntity.getId()))
                                .map(contentEntity -> new DocumentAndContentEntities(documentEntity, contentEntity))
                )
                .flatMap(entities -> updateEntity(entities, update))
                .map(documentMapper::map);
    }

    private Mono<DocumentAndContentEntities> updateEntity(DocumentAndContentEntities entities, DocumentUpdate update) {
        final boolean contentChanged = entities.contentHasChanged();
        final LocalDateTime now = temporalHelper.toDatabaseTime(temporalHelper.now());
        if(update.getTitle() != null) {
            entities.documentEntity().setTitle(update.getTitle());
        }
        if(contentChanged) {
            entities.documentEntity().setContentId(entities.contentEntity().getId());
            entities.documentEntity().setContentModified(now);
        }
        entities.documentEntity().setModified(now);
        return documentRepository.save(entities.documentEntity())
                .map(entities::withDocumentEntity)
                .flatMap(contentChanged ? indexHelper::reindexDocument : indexHelper::reindexDocumentMetadata);
    }


    @Override
    public Mono<Void> delete(UUID uuid) {
        return documentRepository.findByUuid(uuid.toString()).flatMap(entity ->
            documentRepository.delete(entity).then(documentSearchRepository.deleteById(entity.getId()))
        );
    }

    private Mono<Document> create(String title, ContentEntity contentEntity) {
        final ZonedDateTime now = temporalHelper.now();
        final UUID uuid = uuidHelper.createUuid();
        final DocumentEntity entity = new DocumentEntity();
        final LocalDateTime localNow = temporalHelper.toDatabaseTime(now);
        entity.setContentModified(localNow);
        entity.setContentId(contentEntity.getId());
        entity.setCreated(localNow);
        entity.setModified(localNow);
        entity.setTitle(title);
        entity.setUuid(uuid.toString());

        entity.setContentIndexStatus(indexHelper.canIndex(contentEntity.getMimeType()) ? ContentIndexStatus.WAITING : ContentIndexStatus.UNSUPPORTED);
        return documentRepository.save(entity)
                .flatMap(d -> indexHelper.indexDocument(new DocumentAndContentEntities(d, contentEntity)))
                .map(documentMapper::map);
    }
}
