package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.CustomRenditionRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.db.repository.RenditionRepository;
import org.documents.documents.file.TypedFileReference;
import org.documents.documents.helper.DownloadHelper;
import org.documents.documents.helper.EventHelper;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.mapper.RenditionMapper;
import org.documents.documents.model.api.Rendition;
import org.documents.documents.model.exception.NotFoundException;
import org.documents.documents.service.RenditionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RenditionServiceImpl implements RenditionService {

    private final ContentRepository contentRepository;
    private final CustomRenditionRepository customRenditionRepository;
    private final DocumentRepository documentRepository;
    private final DownloadHelper downloadHelper;
    private final EventHelper eventHelper;
    private final RenditionHelper renditionHelper;
    private final RenditionMapper renditionMapper;
    private final RenditionRepository renditionRepository;

    @Override
    public Flux<Rendition> listByDocumentUuid(UUID documentUuid) {
        return documentRepository.findByUuid(documentUuid.toString())
                .map(DocumentEntity::getContentId)
                .flatMapMany(renditionRepository::findByContentId)
                .map(renditionMapper::map);
    }

    @Override
    public Mono<Page<Rendition>> listByMimeType(String mimeType, Pageable pageable) {
        return renditionRepository.findByMimeType(mimeType, pageable)
                .map(renditionMapper::map)
                .collectList()
                .zipWith(renditionRepository.countByMimeType(mimeType))
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }


    @Override
    public Mono<Page<Rendition>> list(Pageable pageable) {
        return customRenditionRepository.findAll(pageable)
                .map(renditionMapper::map)
                .collectList()
                .zipWith(renditionRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }

    @Override
    public Mono<Rendition> get(UUID renditionUuid) {
        return renditionRepository.findByUuid(renditionUuid.toString())
                .switchIfEmpty(Mono.error(() -> new NotFoundException(Rendition.class, renditionUuid)))
                .map(renditionMapper::map);
    }

    @Override
    public Mono<Void> download(ServerHttpResponse response, UUID uuid) {
        return renditionRepository.findByUuid(uuid.toString())
                .switchIfEmpty(Mono.error(() -> new NotFoundException(Rendition.class, uuid)))
                .flatMap(renditionEntity -> downloadHelper.download(response, renditionEntity.getUuid(), new TypedFileReference(renditionEntity)));
    }

    @Override
    public Mono<Void> delete(UUID uuid) {
        return renditionRepository.findByUuid(uuid.toString())
                .switchIfEmpty(Mono.error(() -> new NotFoundException(Rendition.class, uuid)))
                .flatMap(renditionRepository::delete)
                .then(eventHelper.notifyRenditionDeleted(uuid));
    }

    @Override
    public Mono<Rendition> getOrRequest(UUID documentUuid, String mimeType) {
        return documentRepository.findByUuid(documentUuid.toString())
                .map(DocumentEntity::getContentId)
                .flatMap(contentRepository::findById)
                .flatMap(contentEntity -> renditionHelper.getOrRequestRendition(contentEntity, mimeType))
                .map(renditionMapper::map);
    }
}
