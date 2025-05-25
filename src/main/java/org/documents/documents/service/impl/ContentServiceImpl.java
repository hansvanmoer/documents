package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.entity.ContentEntity;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.mapper.ContentMapper;
import org.documents.documents.model.rest.Content;
import org.documents.documents.repository.ContentRepository;
import org.documents.documents.service.ContentService;
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

@AllArgsConstructor
@Service
@Slf4j
public class ContentServiceImpl implements ContentService {

    private final ContentMapper contentMapper;
    private final ContentRepository contentRepository;
    private final FileSettings fileSettings;
    private final TemporalHelper temporalHelper;
    private final UuidHelper uuidHelper;

    @Override
    public Mono<Content> upload(MediaType mimeType, Flux<DataBuffer> content) {
        final ZonedDateTime now = temporalHelper.now();
        final UUID uuid = uuidHelper.createUuid();
        return storeContent(content, uuid, now)
                .flatMap(path -> storeEntity(uuid, now, mimeType, path))
                .map(contentMapper::map);
    }

    private Mono<Path> storeContent(Flux<DataBuffer> content, UUID uuid, ZonedDateTime now) {
            return createFile(uuid, now).flatMap(path ->
                DataBufferUtils.write(content, path)
                        .then(Mono.just(fileSettings.getPath().relativize(path)))
            );
    }

    private Mono<Path> createFile(UUID uuid, ZonedDateTime now) {
        final Path parent = fileSettings.getPath()
                .resolve(Integer.toString(now.getYear()))
                .resolve(Integer.toString(now.getMonthValue()))
                .resolve(Integer.toString(now.getDayOfMonth()));
        return Mono.fromCallable(() -> {
            Files.createDirectories(parent);
            final Path path = parent.resolve(uuid.toString());
            Files.createFile(path);
            return path;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<ContentEntity> storeEntity(UUID uuid, ZonedDateTime now, MediaType mediaType, Path path) {
        final ContentEntity contentEntity = new ContentEntity();
        contentEntity.setUuid(uuid.toString());
        contentEntity.setCreated(temporalHelper.toDatabaseTime(now));
        contentEntity.setPath(path.toString());
        contentEntity.setMimeType(mediaType.toString());
        return contentRepository.save(contentEntity);
    }
}
