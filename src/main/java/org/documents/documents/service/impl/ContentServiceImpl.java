package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.file.FileStore;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.mapper.ContentMapper;
import org.documents.documents.model.api.Content;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.service.ContentService;
import org.documents.documents.service.DocumentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Slf4j
public class ContentServiceImpl implements ContentService {

    private final FileStore fileStore;
    private final ContentMapper contentMapper;
    private final ContentRepository contentRepository;
    private final TemporalHelper temporalHelper;
    private final UuidHelper uuidHelper;
    private final DocumentService documentService;

    public ContentServiceImpl(
            @Qualifier("contentFileStore") FileStore fileStore,
            ContentMapper contentMapper,
            ContentRepository contentRepository,
            TemporalHelper temporalHelper,
            UuidHelper uuidHelper,
            DocumentService documentService) {
        this.fileStore = fileStore;
        this.contentMapper = contentMapper;
        this.contentRepository = contentRepository;
        this.temporalHelper = temporalHelper;
        this.uuidHelper = uuidHelper;
        this.documentService = documentService;
    }

    @Override
    public Mono<Content> upload(MediaType mimeType, Flux<DataBuffer> content) {
        return fileStore.create(content)
                .flatMap(uuid -> storeEntity(uuid, mimeType))
                .map(contentMapper::map);
    }

    private Mono<ContentEntity> storeEntity(UUID uuid, MediaType mediaType) {
        final ContentEntity contentEntity = new ContentEntity();
        contentEntity.setUuid(uuid.toString());
        contentEntity.setCreated(temporalHelper.toDatabaseTime(temporalHelper.now()));
        contentEntity.setMimeType(mediaType.toString());
        return contentRepository.save(contentEntity);
    }
}
