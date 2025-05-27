package org.documents.documents.file.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.file.FileStore;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.helper.FileHelper;
import org.documents.documents.helper.MimeTypeHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.model.exception.ErrorCode;
import org.documents.documents.model.exception.InternalServerErrorException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@AllArgsConstructor
public class TransformFileStoreImpl implements TransformFileStore {

    final Path basePath;
    final FileHelper fileHelper;
    final MimeTypeHelper mimeTypeHelper;
    final UuidHelper uuidHelper;

    @Override
    public Path getWorkingDirectory() {
        return basePath;
    }

    @Override
    public String getFileName(UUID uuid, String mimeType) {
        return createPath(uuid, mimeType).getFileName().toString();
    }

    @Override
    public Path getFilePath(UUID uuid, String mimeType) {
        return createPath(uuid, mimeType);
    }

    @Override
    public UUID create(Path path, String mimeType) {
        final UUID uuid = uuidHelper.createUuid();
        final Path destPath = createPath(uuid, mimeType);
        try {
            Files.copy(path, destPath);
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorCode.FILE_COPY_FAILED, e, "file copy to transform store failed");
        }
        return uuid;
    }

    @Override
    public void delete(UUID uuid, String mimeType) {
        final Path path = createPath(uuid, mimeType);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorCode.FILE_DELETE_FAILED, e, "file delete at transform store failed");
        }
    }

    private Path createPath(UUID uuid, String mimeType) {
        final String extension = mimeTypeHelper.getExtension(mimeType).orElseGet(String::new);
        return basePath.resolve(uuid + extension);
    }

    @Override
    public Mono<UUID> copyAndDelete(UUID uuid, String mimeType, FileStore destination) {
        if(destination.isLocal()) {
            return Mono.fromCallable(() -> {
                final Path path = createPath(uuid, mimeType);
                final UUID createdUuid = destination.create(path);
                delete(uuid, mimeType);
                return createdUuid;
            }).subscribeOn(Schedulers.boundedElastic());
        } else {
            return destination.create(fileHelper.readFromPath(createPath(uuid, mimeType)))
                    .flatMap(createdUuid -> Mono.fromCallable(() -> {
                        delete(uuid, mimeType);
                        return uuid;
                    }).subscribeOn(Schedulers.boundedElastic()));
        }
    }
}
