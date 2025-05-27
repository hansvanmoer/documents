package org.documents.documents.helper;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.file.FileReference;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface RenditionHelper {

    Mono<RenditionEntity> storeRendition(ContentEntity content, String mimeType, UUID transformedFileUuid);

    Mono<FileReference> getFile(ContentEntity contentEntity, String mimeType);

    Mono<FileReference> getOrRequestFile(ContentEntity contentEntity, String mimeType);

    boolean isSupported(String sourceMimeType, String targetMimeType);

}
