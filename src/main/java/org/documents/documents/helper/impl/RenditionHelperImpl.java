package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.file.FileReference;
import org.documents.documents.file.FileStore;
import org.documents.documents.file.FileStoreType;
import org.documents.documents.file.TransformFileStore;
import org.documents.documents.helper.*;
import org.documents.documents.db.repository.RenditionRepository;
import org.documents.documents.transform.TransformRegistry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@AllArgsConstructor
@Component
public class RenditionHelperImpl implements RenditionHelper {

    private final FileStore renditionFileStore;
    private final RenditionRepository renditionRepository;
    private final RequestTransformHelper requestTransformHelper;
    private final TemporalHelper temporalHelper;
    private final TransformFileStore transformFileStore;
    private final TransformRegistry transformRegistry;

    @Override
    public Mono<RenditionEntity> storeRendition(ContentEntity content, String mimeType, UUID transformedFileUuid) {
        return transformFileStore.copyAndDelete(transformedFileUuid, mimeType, renditionFileStore)
                .flatMap(uuid -> {
                    final RenditionEntity entity = new RenditionEntity();
                    entity.setUuid(uuid.toString());
                    entity.setMimeType(mimeType);
                    entity.setCreated(temporalHelper.toDatabaseTime(temporalHelper.now()));
                    entity.setContentId(content.getId());
                    return renditionRepository.save(entity);
                });
    }

    @Override
    public Mono<FileReference> getFile(ContentEntity contentEntity, String mimeType) {
        if(contentEntity.getMimeType().equals(mimeType)) {
            return Mono.just(new FileReference(FileStoreType.CONTENT, UUID.fromString(contentEntity.getUuid())));
        } else {
            return renditionRepository.findByContentIdAndMimeType(contentEntity.getId(), mimeType)
                    .map(renditionEntity -> new FileReference(FileStoreType.RENDITION, UUID.fromString(renditionEntity.getUuid())));
        }
    }

    @Override
    public Mono<FileReference> getOrRequestFile(ContentEntity contentEntity, String mimeType) {
        if(contentEntity.getMimeType().equals(mimeType)) {
            return Mono.just(new FileReference(FileStoreType.CONTENT, UUID.fromString(contentEntity.getUuid())));
        } else {
            return renditionRepository.findByContentIdAndMimeType(contentEntity.getId(), mimeType)
                    .switchIfEmpty(requestTransform(contentEntity, mimeType).then(Mono.empty()))
                    .map(renditionEntity -> new FileReference(FileStoreType.RENDITION, UUID.fromString(renditionEntity.getUuid())));
        }
    }

    @Override
    public boolean isSupported(String sourceMimeType, String targetMimeType) {
        return transformRegistry.getTransform(sourceMimeType, targetMimeType).isPresent();
    }

    private Mono<RenditionEntity> requestTransform(ContentEntity contentEntity, String targetMimeType) {
        return Mono.fromRunnable(
                () -> requestTransformHelper.requestTransform(
                        UUID.fromString(contentEntity.getUuid()),
                        new FileReference(FileStoreType.CONTENT, UUID.fromString(contentEntity.getUuid())),
                        contentEntity.getMimeType(),
                        targetMimeType
                )
        ).subscribeOn(Schedulers.boundedElastic()).then(Mono.empty());
    }
}
