package org.documents.documents.file.impl;

import org.documents.documents.file.FileReference;
import org.documents.documents.file.FileStore;
import org.documents.documents.file.FileStoreRegistry;
import org.documents.documents.file.FileStoreType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

@Component
public class FileStoreRegistryImpl implements FileStoreRegistry {

    private final FileStore contentFileStore;
    private final FileStore renditionFileStore;

    public FileStoreRegistryImpl(
            @Qualifier("contentFileStore")
            FileStore contentFileStore,
            @Qualifier("renditionFileStore")
            FileStore renditionFileStore
    ) {
        this.contentFileStore = contentFileStore;
        this.renditionFileStore = renditionFileStore;
    }

    @Override
    public Flux<DataBuffer> read(FileReference fileReference) {
        return getFileStore(fileReference.fileStoreType()).read(fileReference.uuid());
    }

    @Override
    public void copy(FileReference fileReference, Path targetPath) {
        getFileStore(fileReference.fileStoreType()).copy(fileReference.uuid(), targetPath);
    }

    private FileStore getFileStore(FileStoreType fileStoreType) {
        return switch (fileStoreType) {
            case CONTENT -> contentFileStore;
            case RENDITION -> renditionFileStore;
        };
    }
}
