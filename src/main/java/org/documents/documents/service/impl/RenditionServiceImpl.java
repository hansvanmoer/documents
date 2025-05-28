package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.CustomRenditionRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.db.repository.RenditionRepository;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.mapper.RenditionMapper;
import org.documents.documents.model.api.Rendition;
import org.documents.documents.service.RenditionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RenditionServiceImpl implements RenditionService {

    private final ContentRepository contentRepository;
    private final CustomRenditionRepository customRenditionRepository;
    private final DocumentRepository documentRepository;
    private final RenditionHelper renditionHelper;
    private final RenditionMapper renditionMapper;
    private final RenditionRepository renditionRepository;

    @Override
    public Flux<Rendition> listByDocumentUuid(UUID documentUuid) {
        return documentRepository.findByUuid(documentUuid.toString())
                .map(DocumentEntity::getContentId)
                .flatMapMany(renditionRepository::findByContentId)
                .map(renditionMapper::map);
    }

    @Override
    public Flux<Rendition> listByMimeType(String mimeType, Pageable pageable) {
        return renditionRepository.findByMimeType(mimeType, pageable)
                .map(renditionMapper::map);
    }


    @Override
    public Flux<Rendition> list(Pageable pageable) {
        return customRenditionRepository.findAll(pageable)
                .map(renditionMapper::map);
    }

    @Override
    public Mono<Rendition> get(UUID renditionUuid) {
        return renditionRepository.findByUuid(renditionUuid.toString())
                .map(renditionMapper::map);
    }

    @Override
    public Mono<Void> delete(UUID rendtionUuid) {
        return renditionRepository.deleteByUuid(rendtionUuid.toString());
    }

    @Override
    public Mono<Rendition> getOrRequest(UUID documentUuid, String mimeType) {
        return documentRepository.findByUuid(documentUuid.toString())
                .map(DocumentEntity::getContentId)
                .flatMap(contentRepository::findById)
                .flatMap(contentEntity -> renditionHelper.getOrRequestRendition(contentEntity, mimeType))
                .map(renditionMapper::map);
    }
}
