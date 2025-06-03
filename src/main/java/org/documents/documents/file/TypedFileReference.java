package org.documents.documents.file;

import lombok.Getter;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.RenditionEntity;

import java.util.UUID;

@Getter
public class TypedFileReference {
    private final FileReference fileReference;
    private final String mimeType;

    public TypedFileReference(FileReference fileReference, String mimeType) {
        this.fileReference = fileReference;
        this.mimeType = mimeType;
    }

    public TypedFileReference(ContentEntity contentEntity) {
        this.fileReference = new FileReference(FileStoreType.CONTENT, UUID.fromString(contentEntity.getUuid()));
        this.mimeType = contentEntity.getMimeType();
    }

    public TypedFileReference(RenditionEntity renditionEntity) {
        this.fileReference = new FileReference(FileStoreType.RENDITION, UUID.fromString(renditionEntity.getUuid()));
        this.mimeType = renditionEntity.getMimeType();
    }
}