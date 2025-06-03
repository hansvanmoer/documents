package org.documents.documents.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.repository.CustomContentRepository;
import org.documents.documents.file.FileStore;
import org.documents.documents.file.TypedFileReference;
import org.documents.documents.helper.DownloadHelper;
import org.documents.documents.helper.EventHelper;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.mapper.ContentMapper;
import org.documents.documents.model.api.Content;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.service.ContentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContentServiceImpl implements ContentService {

    private final FileStore fileStore;
    private final ContentMapper contentMapper;
    private final ContentRepository contentRepository;
    private final CustomContentRepository customContentRepository;
    private final DownloadHelper downloadHelper;
    private final EventHelper eventHelper;
    private final TemporalHelper temporalHelper;

    public ContentServiceImpl(
            @Qualifier("contentFileStore") FileStore fileStore,
            ContentMapper contentMapper,
            ContentRepository contentRepository,
            CustomContentRepository customContentRepository,
            EventHelper eventHelper,
            DownloadHelper downloadHelper,
            TemporalHelper temporalHelper
    ) {
        this.fileStore = fileStore;
        this.contentMapper = contentMapper;
        this.contentRepository = contentRepository;
        this.customContentRepository = customContentRepository;
        this.downloadHelper = downloadHelper;
        this.eventHelper = eventHelper;
        this.temporalHelper = temporalHelper;
    }

    @Override
    public Mono<Content> upload(MediaType mimeType, Flux<DataBuffer> content) {
        return fileStore.create(content)
                .flatMap(uuid -> storeEntity(uuid, mimeType))
                .map(contentMapper::map)
                .flatMap(c -> eventHelper.notifyContentCreated(c).thenReturn(c));
    }

    @Override
    public Mono<Content> get(UUID uuid) {
        return contentRepository.findByUuid(uuid.toString()).map(contentMapper::map);
    }

    @Override
    public Mono<Page<Content>> list(Pageable pageable) {
        return customContentRepository.findAll(pageable)
                .map(contentMapper::map)
                .collect(Collectors.toList())
                .zipWith(contentRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }

    @Override
    public Mono<Void> download(ServerHttpResponse response, UUID uuid) {
        return contentRepository.findByUuid(uuid.toString())
                .flatMap(contentEntity -> downloadHelper.download(response, contentEntity.getUuid(), new TypedFileReference(contentEntity)));
    }

    private Mono<ContentEntity> storeEntity(UUID uuid, MediaType mediaType) {
        final ContentEntity contentEntity = new ContentEntity();
        contentEntity.setUuid(uuid.toString());
        contentEntity.setCreated(temporalHelper.toDatabaseTime(temporalHelper.now()));
        contentEntity.setMimeType(mediaType.toString());
        return contentRepository.save(contentEntity);
    }
}
