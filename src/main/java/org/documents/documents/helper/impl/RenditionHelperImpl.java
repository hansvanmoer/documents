package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.entity.ContentEntity;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.helper.RequestTransformHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.repository.RenditionRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Path;
import java.util.UUID;

@AllArgsConstructor
@Component
public class RenditionHelperImpl implements RenditionHelper {

    private final RenditionRepository renditionRepository;
    private final RequestTransformHelper requestTransformHelper;

    @Override
    public Mono<ContentEntity> getOrRequestRendition(ContentEntity entity, MediaType mediaType) {
        if(mediaType.isCompatibleWith(MediaType.parseMediaType(entity.getMimeType()))) {
            return Mono.just(entity);
        } else {
            return renditionRepository.getRenditionForMimeType(entity.getId(), entity.getMimeType())
                    .switchIfEmpty(this.requestTransform(entity, mediaType).then(Mono.empty()));
        }
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
