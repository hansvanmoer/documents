package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.file.*;
import org.documents.documents.helper.*;
import org.documents.documents.model.ContentAndRenditionEntities;
import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.transform.Transform;
import org.documents.documents.transform.TransformRegistry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
@Slf4j
public class RunTransformHelperImpl implements RunTransformHelper {

    private final FileStoreRegistry fileStoreRegistry;
    private final RenditionHelper renditionHelper;
    private final TransformFileStore transformFileStore;
    private final TransformRegistry transformRegistry;

    @Override
    public void runTransform(UUID contentUuid, String targetMimeType) {
        final ContentAndRenditionEntities entities = Objects.requireNonNull(renditionHelper.getContentAndRenditions(contentUuid).block());
        final String sourceMimeType = entities.getContentEntity().getMimeType();
        if(!entities.getRenditionEntitiesByMimeType().containsKey(targetMimeType)) {
            try {
                final Optional<Transform> foundTransform = transformRegistry.getTransform(sourceMimeType, targetMimeType);
                if (foundTransform.isPresent()) {
                    final Transform transform = foundTransform.get();
                    final TypedFileReference bestFileReference = getBestFileReference(entities, transform, targetMimeType);
                    final UUID uuid = fileStoreRegistry.copyToTransformFileStore(bestFileReference.getFileReference(), bestFileReference.getMimeType());
                    final TransformResult result = transform.transform(uuid, bestFileReference.getMimeType(), targetMimeType);
                    if (result.isSuccess()) {
                        onSuccess(contentUuid, uuid, result.getOutputMimeTypes());
                    } else {
                        onFailure(contentUuid, result.getErrorMessage());
                    }
                    transformFileStore.delete(uuid, sourceMimeType);
                } else {
                    log.warn("no transform found for {} -> {}", sourceMimeType, targetMimeType);
                }
            } catch(Exception e) {
                log.error("failed to run transform", e);
            }
        }
    }
    private TypedFileReference getBestFileReference(ContentAndRenditionEntities entities, Transform transform, String targetMimeType) {
        final List<String> preferredMimeTypes = transform.getPreferredSourceMimeTypes(targetMimeType);
        for(String preferredMimeType : preferredMimeTypes) {
            if (entities.getContentEntity().getMimeType().equals(preferredMimeType)) {
                return new TypedFileReference(entities.getContentEntity());
            } else {
                final RenditionEntity renditionEntity = entities.getRenditionEntitiesByMimeType().get(preferredMimeType);
                if(renditionEntity != null) {
                    return new TypedFileReference(renditionEntity);
                }
            }
        }
        return new TypedFileReference(entities.getContentEntity());
    }

    private void onSuccess(UUID contentUuid, UUID transformedUuid, Set<String> outputMimeTypes) {
        final List<RenditionEntity> renditions = Objects.requireNonNull(storeRenditions(contentUuid, transformedUuid, outputMimeTypes)
                .collect(Collectors.toList())
                .block()
        );
        for(RenditionEntity renditionEntity : renditions) {
            log.debug("Rendition {} for mime type {} of content UUID {} successfully stored", renditionEntity.getUuid(), renditionEntity.getMimeType(), contentUuid);
        }
    }

    private Flux<RenditionEntity> storeRenditions(UUID contentUuid, UUID transformedUuid, Set<String> outputMimeTypes) {
        return renditionHelper.getContentAndRenditions(contentUuid)
                .flatMapMany(entities ->
                        Flux.fromIterable(outputMimeTypes)
                                .filter(outputMimeType -> !entities.containsRendition(outputMimeType))
                                .flatMap(outputMimeType -> renditionHelper.storeRendition(entities.getContentEntity(), outputMimeType, transformedUuid))
                );
    }

    private void onFailure(UUID uuid, String message) {
        log.error("failure of transform {}: {}", uuid, message);
    }
}
