package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.helper.FileHelper;
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

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Component
@Slf4j
public class IndexHelperImpl implements IndexHelper {

    private final ContentRepository contentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final DocumentSearchRepository documentSearchRepository;
    private final FileHelper fileHelper;
    private final RenditionHelper renditionHelper;

    @Override
    public Mono<DocumentEntity> indexDocument(DocumentEntity documentEntity, ContentEntity contentEntity) {
        return renditionHelper.getOrRequestFile(contentEntity, MediaType.TEXT_PLAIN_VALUE)
                .flatMap(file -> fileHelper.readToString(file, StandardCharsets.UTF_8))
                .flatMap(text -> indexWithContent(documentEntity, text))
                .switchIfEmpty(Mono.defer(() -> indexMetadata(documentEntity)));
    }

    @Override
    public Mono<DocumentEntity> indexDocumentIfRenditionExists(DocumentEntity documentEntity, ContentEntity contentEntity) {
        return renditionHelper.getFile(contentEntity, MediaType.TEXT_PLAIN_VALUE)
                .flatMap(file -> fileHelper.readToString(file, StandardCharsets.UTF_8))
                .flatMap(text -> indexWithContent(documentEntity, text))
                .switchIfEmpty(Mono.defer(() -> indexMetadata(documentEntity)));
    }

    @Override
    public Mono<DocumentEntity> indexDocument(DocumentEntity documentEntity) {
        return contentRepository.findById(documentEntity.getContentId())
                .flatMap(contentEntity -> indexDocument(documentEntity, contentEntity));
    }

    @Override
    public Mono<DocumentEntity> indexDocumentIfRenditionExists(DocumentEntity documentEntity) {
        return contentRepository.findById(documentEntity.getContentId())
                .flatMap(contentEntity -> indexDocumentIfRenditionExists(documentEntity, contentEntity));
    }

    private Mono<DocumentEntity> indexMetadata(DocumentEntity documentEntity) {
        final DocumentSearchDocument document = documentMapper.mapToSearchDocument(documentEntity, "");
        return documentSearchRepository.save(document, RefreshPolicy.IMMEDIATE)
                .thenReturn(documentEntity);
    }

    private Mono<DocumentEntity> indexWithContent(DocumentEntity documentEntity, String text) {
        final DocumentSearchDocument document = documentMapper.mapToSearchDocument(documentEntity, text);
        return documentSearchRepository.save(document, RefreshPolicy.IMMEDIATE)
                .then(Mono.just(documentEntity).flatMap(this::updateEntityAfterIndexWithContent));
    }

    private Mono<DocumentEntity> updateEntityAfterIndexWithContent(DocumentEntity documentEntity) {
        if(documentEntity.getContentIndexStatus() != ContentIndexStatus.INDEXED) {
            documentEntity.setContentIndexStatus(ContentIndexStatus.INDEXED);
            return documentRepository.save(documentEntity);
        } else {
            return Mono.just(documentEntity);
        }
    }
}
