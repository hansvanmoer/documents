package org.documents.documents.file.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.file.FileStore;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.model.exception.ErrorCode;
import org.documents.documents.model.exception.InternalServerErrorException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public class LocalFileStore implements FileStore {

    private final Path basePath;
    private final DataBufferFactory dataBufferFactory;
    private final FileSettings fileSettings;
    private final UuidHelper uuidHelper;

    @Override
    public UUID create(Path path) {
        final UUID uuid = uuidHelper.createUuid();
        final Path destPath = createPath(uuid);
        try {
            Files.createDirectories(destPath.getParent());
            Files.copy(path, destPath);
        } catch(IOException e) {
            throw new InternalServerErrorException(ErrorCode.FILE_COPY_FAILED, e, "could not copy file %s from path %s", uuid, path);
        }
        return uuid;
    }

    @Override
    public Mono<UUID> create(Flux<DataBuffer> content) {
        final UUID uuid = uuidHelper.createUuid();
        return createFile(uuid).flatMap(path ->
                DataBufferUtils.write(content, path)
                        .thenReturn(uuid)
        );
    }

    @Override
    public Flux<DataBuffer> read(UUID uuid) {
        return DataBufferUtils.read(createPath(uuid), dataBufferFactory, fileSettings.getReadBufferSize());
    }

    @Override
    public UUID copyTo(UUID uuid, FileStore target) {
        if(target.isLocal()) {
            final Path srcPath = createPath(uuid);
            return target.create(srcPath);
        } else {
            return Objects.requireNonNull(target.create(read(uuid)).block());
        }
    }

    @Override
    public UUID copyTo(UUID uuid, String mimeType, TransformFileStore target) {
        final Path srcPath = createPath(uuid);
        return target.create(srcPath, mimeType);
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    private Path createPath(UUID uuid) {
        final String uuidString = uuid.toString();
        return basePath
                .resolve(uuidString.substring(0, 2))
                .resolve(uuidString.substring(2, 4))
                .resolve(uuidString.substring(4, 6))
                .resolve(uuidString);
    }

    private Mono<Path> createFile(UUID uuid) {
        return Mono.fromCallable(() -> {
            final Path path = createPath(uuid);
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return path;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
