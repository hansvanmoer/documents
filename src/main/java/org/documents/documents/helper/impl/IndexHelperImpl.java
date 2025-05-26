package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.helper.ContentHelper;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.model.api.ContentIndexStatus;
import org.documents.documents.search.document.DocumentSearchDocument;
import org.documents.documents.search.repository.DocumentSearchRepository;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
@Slf4j
public class IndexHelperImpl implements IndexHelper {

    private final ContentHelper contentHelper;
    private final ContentRepository contentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentSearchRepository documentSearchRepository;
    private final RenditionHelper renditionHelper;
    private final DocumentRepository documentRepository;

    @Override
    public Mono<DocumentEntity> indexDocument(DocumentEntity documentEntity) {
        final Mono<ContentEntity> contentProducer = contentRepository.findById(documentEntity.getContentId())
                        .flatMap(contentEntity -> renditionHelper.getRendition(contentEntity, MediaType.TEXT_PLAIN));
        return contentProducer
                .flatMap(content -> indexWithContent(documentEntity, content))
                .switchIfEmpty(Mono.defer(() -> indexMetadata(documentEntity)));
    }

    @Override
    public Mono<DocumentEntity> indexDocumentOrScheduleIndexation(DocumentEntity documentEntity, ContentEntity contentEntity) {
        final Mono<ContentEntity> contentProducer = renditionHelper.getOrRequestRendition(contentEntity, MediaType.TEXT_PLAIN);
        return contentProducer
                .flatMap(content -> indexWithContent(documentEntity, content))
                .switchIfEmpty(Mono.defer(() -> indexMetadata(documentEntity)));
    }

    private Mono<DocumentEntity> indexMetadata(DocumentEntity documentEntity) {
        final DocumentSearchDocument document = documentMapper.mapToSearchDocument(documentEntity, "");
        return documentSearchRepository.save(document, RefreshPolicy.IMMEDIATE)
                .thenReturn(documentEntity);
    }

    private Mono<DocumentEntity> indexWithContent(DocumentEntity documentEntity, ContentEntity contentEntity) {
        return contentHelper.readContentAsString(contentEntity)
                .map(text -> documentMapper.mapToSearchDocument(documentEntity, text))
                .flatMap(document -> documentSearchRepository.save(document, RefreshPolicy.IMMEDIATE))
                .then(Mono.defer(() -> {
                    documentEntity.setContentIndexStatus(ContentIndexStatus.INDEXED);
                    return documentRepository.save(documentEntity);
                }));
    }
}
