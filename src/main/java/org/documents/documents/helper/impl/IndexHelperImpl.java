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
import org.documents.documents.model.DocumentToIndex;
import org.documents.documents.db.entity.ContentIndexStatus;
import org.documents.documents.search.repository.CustomDocumentSearchRepository;
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
    private final CustomDocumentSearchRepository customDocumentSearchRepository;
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final DocumentSearchRepository documentSearchRepository;
    private final FileHelper fileHelper;
    private final FileStoreRegistry fileStoreRegistry;
    private final RenditionHelper renditionHelper;

    @Override
    public Mono<DocumentAndContentEntities> indexDocument(DocumentAndContentEntities entities) {
        final ContentEntity contentEntity = entities.contentEntity();
        return indexOrReindexDocument(renditionHelper.getOrRequestFile(contentEntity, MediaType.TEXT_PLAIN_VALUE), entities.toDocumentToIndex());
    }

    @Override
    public Mono<DocumentAndContentEntities> indexDocumentIfRenditionExists(DocumentEntity documentEntity) {
        return contentRepository.findById(documentEntity.getContentId()).flatMap(contentEntity -> indexDocumentIfRenditionExists(new DocumentAndContentEntities(documentEntity, contentEntity)));
    }

    @Override
    public Mono<DocumentAndContentEntities> indexDocumentIfRenditionExists(DocumentAndContentEntities entities) {
        return indexOrReindexDocument(renditionHelper.getFile(entities.contentEntity(), MediaType.TEXT_PLAIN_VALUE), entities.toDocumentToIndex());
    }

    @Override
    public Mono<DocumentAndContentEntities> reindexDocument(DocumentAndContentEntities entities) {
        return indexOrReindexDocument(renditionHelper.getOrRequestFile(entities.contentEntity(), MediaType.TEXT_PLAIN_VALUE), entities.toDocumentToIndex());
    }

    @Override
    public Mono<DocumentAndContentEntities> reindexDocumentMetadata(DocumentAndContentEntities entities) {
        return reindexMetadata(entities.toDocumentToIndex()).map(DocumentToIndex::toDocumentAndContentEntities);
    }

    private Mono<DocumentAndContentEntities> indexOrReindexDocument(Mono<FileReference> fileReferenceProducer, DocumentToIndex entities) {
        return fileReferenceProducer.map(fileStoreRegistry::createFileProxy)
                .flatMap(file -> fileHelper.readToString(file, StandardCharsets.UTF_8))
                .map(entities::withContentAsText)
                .flatMap(this::indexOrReindexWithContent)
                .switchIfEmpty(indexMetadata(entities))
                .map(DocumentToIndex::toDocumentAndContentEntities);
    }

    private Mono<DocumentToIndex> indexMetadata(DocumentToIndex entities) {
        return documentSearchRepository.save(documentMapper.mapToDocumentSearchDocument(entities), RefreshPolicy.IMMEDIATE)
                .thenReturn(entities);
    }

    private Mono<DocumentToIndex> reindexMetadata(DocumentToIndex entities) {
        return customDocumentSearchRepository.updateDocumentMetadata(
                entities.documentEntity().getId(),
                entities.documentEntity().getTitle()
        ).thenReturn(entities);
    }

    private Mono<DocumentToIndex> indexOrReindexWithContent(DocumentToIndex entities) {
        return documentSearchRepository.save(documentMapper.mapToDocumentSearchDocument(entities), RefreshPolicy.IMMEDIATE)
                .then(updateEntityAfterIndexWithContent(entities));
    }

    private Mono<DocumentToIndex> updateEntityAfterIndexWithContent(DocumentToIndex entities) {
        final DocumentEntity documentEntity = entities.documentEntity();
        if(documentEntity.getContentIndexStatus() != ContentIndexStatus.INDEXED) {
            documentEntity.setContentIndexStatus(ContentIndexStatus.INDEXED);
            return documentRepository.save(documentEntity).map(entities::withDocumentEntity);
        } else {
            return Mono.just(entities);
        }
    }
}
