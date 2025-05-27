package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.config.settings.TransformSettings;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.file.FileReference;
import org.documents.documents.file.FileStoreRegistry;
import org.documents.documents.helper.*;
import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.transform.Transform;
import org.documents.documents.transform.TransformRegistry;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class RunTransformHelperImpl implements RunTransformHelper {

    private final FileStoreRegistry fileStoreRegistry;
    private final MimeTypeHelper mimeTypeHelper;
    private final RenditionHelper renditionHelper;
    private final TransformRegistry transformRegistry;
    private final TransformSettings transformSettings;
    private final ContentRepository contentRepository;
    private final UuidHelper uuidHelper;

    @Override
    public void runTransform(UUID contentUuid, FileReference fileReference, String sourceMimeType, String targetMimeType) {
        try {
            final Optional<Transform> foundTransform = transformRegistry.getTransform(sourceMimeType, targetMimeType);
            if (foundTransform.isPresent()) {
                final Transform transform = foundTransform.get();
                final Optional<String> foundSourceExtension = mimeTypeHelper.getExtension(sourceMimeType);
                if (foundSourceExtension.isPresent()) {
                    final String sourceExtension = foundSourceExtension.get();
                    final Optional<String> foundTargetExtension = mimeTypeHelper.getExtension(targetMimeType);
                    if (foundTargetExtension.isPresent()) {
                        final String targetExtension = foundTargetExtension.get();
                        final Path sourcePath = transformSettings.getPath().resolve(uuidHelper.createUuid() + sourceExtension);
                        fileStoreRegistry.copy(fileReference, sourcePath);
                        final TransformResult result = transform.transform(sourcePath, sourceMimeType, targetMimeType, targetExtension);
                        if (result.isSuccess()) {
                            onSuccess(contentUuid, result.getResultPath(), targetMimeType);
                        } else {
                            onFailure(contentUuid, result.getMessage());
                        }
                        try {
                            Files.deleteIfExists(sourcePath);
                            Files.deleteIfExists(result.getResultPath());
                        } catch (IOException e) {
                            log.error("failed to clean up after transform {}", contentUuid, e);
                        }
                    } else {
                        log.info("no extension found for document {} and target mime type {}", fileReference, targetMimeType);
                    }
                } else {
                    log.info("no extension found for document {} and source mime type {}", fileReference, sourceMimeType);
                }
            } else {
                log.info("no transform found for document {} and mime types {} -> {}", fileReference, sourceMimeType, targetMimeType);
            }
        } catch(Exception e) {
            log.error("failed to run transform", e);
        }
    }

    private void onSuccess(UUID contentUuid, Path result, String targetMimeType) {
        contentRepository.findByUuid(contentUuid.toString())
                .flatMap(contentEntity -> renditionHelper.storeRendition(contentEntity, targetMimeType, result))
                .block();
        log.debug("Rendition successfully stored {} : {}", contentUuid, targetMimeType);
    }

    private void onFailure(UUID uuid, String message) {
        log.error("failure of transform {}: {}", uuid, message);
    }
}
