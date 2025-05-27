package org.documents.documents.file.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.file.FileContent;
import org.documents.documents.file.FileStore;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

import java.util.UUID;

@AllArgsConstructor
public class FileContentImpl implements FileContent {
    private final FileStore fileStore;
    private final UUID uuid;

    @Override
    public Flux<DataBuffer> read() {
        return fileStore.read(uuid);
    }
}
