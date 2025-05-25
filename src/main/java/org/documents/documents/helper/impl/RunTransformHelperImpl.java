package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.config.settings.FileSettings;
import org.documents.documents.config.settings.TransformSettings;
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

    private final FileSettings fileSettings;
    private final MimeTypeHelper mimeTypeHelper;
    private final TransformRegistry transformRegistry;
    private final TransformSettings transformSettings;
    private final UuidHelper uuidHelper;

    @Override
    public void runTransform(Path path, String sourceMimeType, String targetMimeType) {
        final Optional<Transform> foundTransform = transformRegistry.getTransform(sourceMimeType, targetMimeType);
        if(foundTransform.isPresent()) {
            final Transform transform = foundTransform.get();
            final Optional<String> foundSourceExtension = mimeTypeHelper.getExtension(sourceMimeType);
            if(foundSourceExtension.isPresent()) {
                final String sourceExtension = foundSourceExtension.get();
                final Optional<String> foundTargetExtension = mimeTypeHelper.getExtension(targetMimeType);
                if(foundTargetExtension.isPresent()) {
                    final String targetExtension = foundTargetExtension.get();
                    final UUID uuid = uuidHelper.createUuid();
                    final Path contentPath = fileSettings.getPath().resolve(path);
                    final Path sourcePath = transformSettings.getPath().resolve(uuid + sourceExtension);
                    try {
                        Files.copy(contentPath, sourcePath);
                        final TransformResult result = transform.transform(sourcePath, sourceMimeType, targetMimeType, targetExtension);
                        if(!result.isSuccess()) {
                            log.error(result.getMessage());
                        }
                    } catch(IOException e) {
                        log.error("could not copy content", e);
                    }
                } else {
                    log.info("no extension found for document {} and target mime type {}", path, targetMimeType);
                }
            } else {
                log.info("no extension found for document {} and source mime type {}", path, sourceMimeType);
            }
        } else {
            log.info("no transform found for document {} and mime types {} -> {}", path, sourceMimeType, targetMimeType);
        }
    }
}
