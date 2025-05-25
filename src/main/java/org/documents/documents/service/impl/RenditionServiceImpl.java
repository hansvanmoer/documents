package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.entity.ContentEntity;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.model.exception.NotFoundException;
import org.documents.documents.repository.ContentRepository;
import org.documents.documents.service.RenditionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RenditionServiceImpl implements RenditionService {

    private final ContentRepository contentRepository;
    private final RenditionHelper renditionHelper;

    @Override
    public Mono<UUID> requestRendition(UUID contentUuid, MediaType mediaType) {
        return contentRepository.findByUuid(contentUuid.toString())
                .switchIfEmpty(Mono.error(new NotFoundException("content not found for rendition", contentUuid)))
                .flatMap(contentEntity -> renditionHelper.getOrRequestRendition(contentEntity, mediaType))
                .map(ContentEntity::getUuid)
                .map(UUID::fromString);
    }
}
