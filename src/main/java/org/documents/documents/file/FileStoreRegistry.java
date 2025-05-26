package org.documents.documents.file;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

public interface FileStoreRegistry {

    Flux<DataBuffer> read(FileReference fileReference);

    void copy(FileReference fileReference, Path targetPath);
}
