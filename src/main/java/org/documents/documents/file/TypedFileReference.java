package org.documents.documents.file;

import java.util.UUID;

public record TypedFileReference(FileStoreType fileStoreType, UUID uuid, String mimeType) {
    public FileReference toFileReference() {
        return new FileReference(fileStoreType, uuid);
    }
}