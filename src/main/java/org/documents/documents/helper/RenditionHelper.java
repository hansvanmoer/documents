package org.documents.documents.helper;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.file.FileReference;
import org.documents.documents.file.TypedFileReference;
import org.documents.documents.model.ContentAndRenditionEntities;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface RenditionHelper {

    Mono<ContentAndRenditionEntities> getContentAndRenditions(UUID contentUuid);
    
    Mono<RenditionEntity> getOrRequestRendition(ContentEntity contentEntity, String mimeType);

    Mono<RenditionEntity> storeRendition(ContentEntity content, String mimeType, UUID transformedFileUuid);

    Mono<FileReference> getFile(ContentEntity contentEntity, String mimeType);

    default Mono<TypedFileReference> getTypedFile(ContentEntity contentEntity, String mimeType) {
        return getFile(contentEntity, mimeType).map(fileReference -> new TypedFileReference(fileReference, mimeType));
    }

    Mono<FileReference> getOrRequestFile(ContentEntity contentEntity, String mimeType);

    boolean isSupported(String sourceMimeType, String targetMimeType);

}
