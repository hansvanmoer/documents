package org.documents.documents.file.impl;

import lombok.Getter;
import org.documents.documents.file.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FileStoreRegistryImpl implements FileStoreRegistry {

    private final FileStore contentFileStore;
    private final FileStore renditionFileStore;
    private final TransformFileStore transformFileStore;

    public FileStoreRegistryImpl(
            @Qualifier("contentFileStore")
            FileStore contentFileStore,
            @Qualifier("renditionFileStore")
            FileStore renditionFileStore,
            TransformFileStore transformFileStore
    ) {
        this.contentFileStore = contentFileStore;
        this.renditionFileStore = renditionFileStore;
        this.transformFileStore = transformFileStore;
    }

    @Override
    public FileStore getFileStore(FileStoreType type) {
        return switch (type) {
            case CONTENT -> contentFileStore;
            case RENDITION -> renditionFileStore;
        };
    }
}
