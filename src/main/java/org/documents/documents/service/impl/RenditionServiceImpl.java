package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.db.repository.RenditionRepository;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.mapper.RenditionMapper;
import org.documents.documents.model.api.Rendition;
import org.documents.documents.service.RenditionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RenditionServiceImpl implements RenditionService {

    private final ContentRepository contentRepository;
    private final DocumentRepository documentRepository;
    private final RenditionHelper renditionHelper;
    private final RenditionMapper renditionMapper;
    private final RenditionRepository renditionRepository;

    @Override
    public Flux<Rendition> getDocumentRenditions(UUID documentUuid) {
        return documentRepository.findByUuid(documentUuid.toString())
                .map(DocumentEntity::getContentId)
                .flatMapMany(renditionRepository::findByContentId)
                .map(renditionMapper::map);
    }

    @Override
    public Mono<Rendition> getOrRequestRendition(UUID documentUuid, String mimeType) {
        return documentRepository.findByUuid(documentUuid.toString())
                .map(DocumentEntity::getContentId)
                .flatMap(contentRepository::findById)
                .flatMap(contentEntity -> renditionHelper.getOrRequestRendition(contentEntity, mimeType))
                .map(renditionMapper::map);
    }
}
