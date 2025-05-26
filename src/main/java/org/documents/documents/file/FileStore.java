package org.documents.documents.file;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.UUID;

public interface FileStore {

    Mono<UUID> create(Flux<DataBuffer> content);

    Flux<DataBuffer> read(UUID uuid);

    void copy(UUID uuid, Path target);

}
