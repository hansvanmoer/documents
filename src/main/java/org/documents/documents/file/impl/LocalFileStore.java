package org.documents.documents.file.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.file.FileStore;
import org.documents.documents.helper.TemporalHelper;
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
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@AllArgsConstructor
public class LocalFileStore implements FileStore {

    private final Path basePath;
    private final DataBufferFactory dataBufferFactory;
    private final FileSettings fileSettings;
    private final UuidHelper uuidHelper;

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
        return DataBufferUtils.read(getPath(uuid), dataBufferFactory, fileSettings.getReadBufferSize());
    }

    @Override
    public void copy(UUID uuid, Path targetPath) {
        final Path sourcePath = getPath(uuid);
        try {
            Files.copy(sourcePath, targetPath);
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorCode.FILE_COPY_FAILED, e, "could not copy file %s", uuid);
        }
    }

    private Path getPath(UUID uuid) {
        final String uuidString = uuid.toString();
        return basePath
                .resolve(uuidString.substring(0, 2))
                .resolve(uuidString.substring(2, 4))
                .resolve(uuidString.substring(4, 6))
                .resolve(uuidString);
    }

    private Mono<Path> createFile(UUID uuid) {
        return Mono.fromCallable(() -> {
            final Path path = getPath(uuid);
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return path;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
