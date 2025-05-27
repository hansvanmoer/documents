package org.documents.documents.file;

import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.UUID;

public interface TransformFileStore {

    Path getWorkingDirectory();

    String getFileName(UUID uuid, String mimeType);

    UUID create(Path path, String mimeType);

    void delete(UUID uuid, String mimeType);

    Mono<UUID> copyAndDelete(UUID uuid, String mimeType, FileStore destination);
}
