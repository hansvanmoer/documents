package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.file.FileReference;
import org.documents.documents.file.FileStoreRegistry;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.helper.*;
import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.transform.Transform;
import org.documents.documents.transform.TransformRegistry;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class RunTransformHelperImpl implements RunTransformHelper {

    private final FileStoreRegistry fileStoreRegistry;
    private final RenditionHelper renditionHelper;
    private final TransformFileStore transformFileStore;
    private final TransformRegistry transformRegistry;
    private final ContentRepository contentRepository;

    @Override
    public void runTransform(UUID contentUuid, FileReference fileReference, String sourceMimeType, String targetMimeType) {
        try {
            final Optional<Transform> foundTransform = transformRegistry.getTransform(sourceMimeType, targetMimeType);
            if (foundTransform.isPresent()) {
                final Transform transform = foundTransform.get();
                final UUID uuid = fileStoreRegistry.copyToTransformFileStore(fileReference, sourceMimeType);
                final TransformResult result = transform.transform(uuid, sourceMimeType, targetMimeType);
                if (result.success()) {
                    onSuccess(contentUuid, uuid, targetMimeType);
                } else {
                    onFailure(contentUuid, result.message());
                }
                transformFileStore.delete(uuid, sourceMimeType);
            } else {
                log.warn("no transform found for {} -> {}", sourceMimeType, targetMimeType);
            }
        } catch(Exception e) {
            log.error("failed to run transform", e);
        }
    }

    private void onSuccess(UUID contentUuid, UUID transformedUuid, String targetMimeType) {
        contentRepository.findByUuid(contentUuid.toString())
                .flatMap(contentEntity -> renditionHelper.storeRendition(contentEntity, targetMimeType, transformedUuid))
                .block();
        log.debug("Rendition successfully stored {} : {}", contentUuid, targetMimeType);
    }

    private void onFailure(UUID uuid, String message) {
        log.error("failure of transform {}: {}", uuid, message);
    }
}
