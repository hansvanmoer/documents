package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.file.FileReference;
import org.documents.documents.file.FileStoreRegistry;
import org.documents.documents.helper.FileHelper;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.model.DocumentAndContentEntities;
import org.documents.documents.model.api.ContentIndexStatus;
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
    private final FileStoreRegistry fileStoreRegistry;
    private final RenditionHelper renditionHelper;

    @Override
    public Mono<DocumentEntity> indexDocument(DocumentEntity documentEntity, ContentEntity contentEntity) {
        final DocumentAndContentEntities entities = new DocumentAndContentEntities(documentEntity, contentEntity, "");
        return indexDocuments(renditionHelper.getOrRequestFile(contentEntity, MediaType.TEXT_PLAIN_VALUE), entities);
    }

    @Override
    public Mono<DocumentEntity> indexDocumentIfRenditionExists(DocumentEntity documentEntity, ContentEntity contentEntity) {
        final DocumentAndContentEntities entities = new DocumentAndContentEntities(documentEntity, contentEntity, "");
        return indexDocuments(renditionHelper.getFile(contentEntity, MediaType.TEXT_PLAIN_VALUE), entities);
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

    private Mono<DocumentEntity> indexDocuments(Mono<FileReference> fileReferenceProducer, DocumentAndContentEntities entities) {
        return fileReferenceProducer.map(fileStoreRegistry::createFileProxy)
                .flatMap(file -> fileHelper.readToString(file, StandardCharsets.UTF_8))
                .map(entities::withContentAsText)
                .flatMap(this::indexWithContent)
                .switchIfEmpty(Mono.defer(() -> indexMetadata(entities)));
    }

    private Mono<DocumentEntity> indexMetadata(DocumentAndContentEntities entities) {
        return documentSearchRepository.save(documentMapper.mapToDocumentSearchDocument(entities), RefreshPolicy.IMMEDIATE)
                .thenReturn(entities.getDocumentEntity());
    }

    private Mono<DocumentEntity> indexWithContent(DocumentAndContentEntities entities) {
        return documentSearchRepository.save(documentMapper.mapToDocumentSearchDocument(entities), RefreshPolicy.IMMEDIATE)
                .then(Mono.just(entities).flatMap(this::updateEntityAfterIndexWithContent));
    }

    private Mono<DocumentEntity> updateEntityAfterIndexWithContent(DocumentAndContentEntities entities) {
        final DocumentEntity documentEntity = entities.getDocumentEntity();
        if(documentEntity.getContentIndexStatus() != ContentIndexStatus.INDEXED) {
            documentEntity.setContentIndexStatus(ContentIndexStatus.INDEXED);
            return documentRepository.save(documentEntity);
        } else {
            return Mono.just(documentEntity);
        }
    }
}
