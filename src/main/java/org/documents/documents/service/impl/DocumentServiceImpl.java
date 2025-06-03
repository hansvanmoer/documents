package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.documents.documents.file.TypedFileReference;
import org.documents.documents.helper.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
    private final DownloadHelper downloadHelper;
    private final EventHelper eventHelper;
    private final IndexHelper indexHelper;
    private final RenditionHelper renditionHelper;
    private final TemporalHelper temporalHelper;
    private final UuidHelper uuidHelper;

    @Override
    public Mono<Document> create(String title, UUID contentUuid) {
        return contentRepository.findByUuid(contentUuid.toString())
            .switchIfEmpty(Mono.error(new NotFoundException("content for new document not found", contentUuid)))
            .flatMap(content -> create(title, content))
                .flatMap(document -> eventHelper.notifyDocumentCreated(document).thenReturn(document));
    }

    @Override
    public Mono<Document> get(UUID uuid) {
        return customDocumentRepository.findByUuid(uuid).map(documentMapper::map);
    }

    @Override
    public Mono<Page<Document>> list(Pageable pageable) {
        return customDocumentRepository.find(pageable)
                .map(documentMapper::map)
                .collectList()
                .zipWith(documentRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
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
                .map(documentMapper::map)
                .flatMap(document -> eventHelper.notifyDocumentUpdated(document).thenReturn(document));
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
        ).then(eventHelper.notifyDocumentDeleted(uuid));
    }

    @Override
    public Mono<Void> download(ServerHttpResponse response, UUID uuid) {
        return documentRepository.findByUuid(uuid.toString())
                .flatMap(documentEntity ->
                    contentRepository
                            .findById(documentEntity.getContentId())
                            .flatMap(contentEntity ->
                                    downloadHelper.download(response, documentEntity.getTitle(), new TypedFileReference(contentEntity))
                            )
                );
    }

    @Override
    public Mono<Void> download(ServerHttpResponse response, UUID uuid, MediaType mimeType) {
        return documentRepository.findByUuid(uuid.toString())
                .flatMap(documentEntity ->
                        contentRepository
                                .findById(documentEntity.getContentId())
                                .flatMap(contentEntity ->
                                        renditionHelper.getTypedFile(contentEntity, mimeType.toString())
                                                .flatMap(
                                                        fileReference -> downloadHelper.download(response, documentEntity.getTitle(), fileReference)
                                                )
                                )
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
