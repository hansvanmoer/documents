package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.helper.RequestTransformHelper;
import org.documents.documents.db.repository.RenditionRepository;
import org.documents.documents.transform.TransformRegistry;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Path;

@AllArgsConstructor
@Component
public class RenditionHelperImpl implements RenditionHelper {

    private final RenditionRepository renditionRepository;
    private final RequestTransformHelper requestTransformHelper;
    private final TransformRegistry transformRegistry;

    @Override
    public Mono<ContentEntity> getRendition(ContentEntity entity, MediaType mediaType) {
        if(mediaType.isCompatibleWith(MediaType.parseMediaType(entity.getMimeType()))) {
            return Mono.just(entity);
        } else {
            return renditionRepository.getRenditionForMimeType(entity.getId(), entity.getMimeType());
        }
    }

    @Override
    public Mono<ContentEntity> getOrRequestRendition(ContentEntity entity, MediaType mediaType) {
        if(mediaType.isCompatibleWith(MediaType.parseMediaType(entity.getMimeType()))) {
            return Mono.just(entity);
        } else {
            return renditionRepository.getRenditionForMimeType(entity.getId(), entity.getMimeType())
                    .switchIfEmpty(this.requestTransform(entity, mediaType).then(Mono.empty()));
        }
    }

    @Override
    public boolean isSupported(String sourceMimeType, String targetMimeType) {
        return transformRegistry.getTransform(sourceMimeType, targetMimeType).isPresent();
    }

    private Mono<Void> requestTransform(ContentEntity entity, MediaType mediaType) {
        final Mono<Object> requestProducer = Mono.fromRunnable(() -> requestTransformHelper.requestTransform(
                Path.of(entity.getPath()),
                MediaType.parseMediaType(entity.getMimeType()),
                mediaType
        )).subscribeOn(Schedulers.boundedElastic());
        return requestProducer.then(Mono.empty());
    }
}
