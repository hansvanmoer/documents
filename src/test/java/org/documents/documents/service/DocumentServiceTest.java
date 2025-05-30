package org.documents.documents.service;

import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.ContentIndexStatus;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.model.DocumentAndContentEntities;
import org.documents.documents.model.api.Document;
import org.documents.documents.model.exception.NotFoundException;
import org.documents.documents.search.repository.DocumentSearchRepository;
import org.documents.documents.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class DocumentServiceTest {

    @TestConfiguration
    public static class DocumentServiceTestConfiguration {
        @Bean
        public DocumentService documentService(
                ContentRepository contentRepository,
                CustomDocumentRepository customDocumentRepository,
                DocumentMapper documentMapper,
                DocumentRepository documentRepository,
                DocumentSearchRepository documentSearchRepository,
                IndexHelper indexHelper,
                TemporalHelper temporalHelper,
                UuidHelper uuidHelper
        ) {
            return new DocumentServiceImpl(
                    contentRepository,
                    customDocumentRepository,
                    documentMapper,
                    documentRepository,
                    documentSearchRepository,
                    indexHelper,
                    temporalHelper,
                    uuidHelper
            );
        }
    }

    @MockitoBean
    private ContentRepository contentRepository;
    @MockitoBean
    private CustomDocumentRepository customDocumentRepository;
    @MockitoBean
    private DocumentMapper documentMapper;
    @MockitoBean
    private DocumentRepository documentRepository;
    @MockitoBean
    private DocumentSearchRepository documentSearchRepository;
    @MockitoBean
    private IndexHelper indexHelper;
    @MockitoBean
    private TemporalHelper temporalHelper;
    @MockitoBean
    private UuidHelper uuidHelper;

    @Autowired
    private DocumentService documentService;

    @Test
    public void testCreateContentNotFound() {
        final String title = "title";
        final UUID contentUuid = UUID.randomUUID();
        Mockito.when(contentRepository.findByUuid(contentUuid.toString())).thenReturn(Mono.empty());
        StepVerifier.create(documentService.create(title, contentUuid))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    public void testCreatePlainTextDocument() {
        final String title = "title";
        final UUID contentUuid = UUID.randomUUID();
        final Long contentId = 1L;
        final String mimeType = MediaType.TEXT_PLAIN_VALUE;
        final ContentEntity contentEntity = new ContentEntity();
        contentEntity.setUuid(contentUuid.toString());
        contentEntity.setId(contentId);
        contentEntity.setMimeType(mimeType);
        Mockito.when(contentRepository.findByUuid(contentUuid.toString())).thenReturn(Mono.just(contentEntity));

        final UUID documentUuid = UUID.randomUUID();
        Mockito.when(uuidHelper.createUuid()).thenReturn(documentUuid);

        final ZonedDateTime now = ZonedDateTime.now();
        Mockito.when(temporalHelper.now()).thenReturn(now);
        final LocalDateTime localNow = LocalDateTime.now();
        Mockito.when(temporalHelper.toDatabaseTime(now)).thenReturn(localNow);

        Mockito.when(indexHelper.canIndex(mimeType)).thenReturn(true);

        final DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setUuid(documentUuid.toString());
        documentEntity.setTitle(title);
        documentEntity.setContentId(contentId);
        documentEntity.setCreated(localNow);
        documentEntity.setModified(localNow);
        documentEntity.setContentModified(localNow);
        documentEntity.setContentIndexStatus(ContentIndexStatus.WAITING);

        final Long documentId = 2L;
        final DocumentEntity createdDocumentEntity = new DocumentEntity();
        createdDocumentEntity.setId(documentId);

        Mockito.when(documentRepository.save(documentEntity)).thenReturn(Mono.just(createdDocumentEntity));

        final DocumentEntity indexedEntity = new DocumentEntity();
        indexedEntity.setId(documentId);
        indexedEntity.setContentIndexStatus(ContentIndexStatus.INDEXED);
        final DocumentAndContentEntities indexedEntities = new DocumentAndContentEntities(indexedEntity, contentEntity);
        Mockito.when(indexHelper.indexDocument(new DocumentAndContentEntities(createdDocumentEntity, contentEntity)))
                        .thenReturn(Mono.just(indexedEntities));

        final Document document = new Document(
                documentUuid,
                now,
                now,
                now,
                contentUuid,
                mimeType,
                title
        );
        Mockito.when(documentMapper.map(indexedEntities)).thenReturn(document);

        StepVerifier.create(documentService.create(title, contentUuid))
                .expectNext(document)
                .expectComplete()
                .verify();
    }

    @Test
    public void testCreateDocumentSupportedInIndex() {
        final String title = "title";
        final UUID contentUuid = UUID.randomUUID();
        final Long contentId = 1L;
        final String mimeType = MediaType.TEXT_PLAIN_VALUE;
        final ContentEntity contentEntity = new ContentEntity();
        contentEntity.setUuid(contentUuid.toString());
        contentEntity.setId(contentId);
        contentEntity.setMimeType(mimeType);
        Mockito.when(contentRepository.findByUuid(contentUuid.toString())).thenReturn(Mono.just(contentEntity));

        final UUID documentUuid = UUID.randomUUID();
        Mockito.when(uuidHelper.createUuid()).thenReturn(documentUuid);

        final ZonedDateTime now = ZonedDateTime.now();
        Mockito.when(temporalHelper.now()).thenReturn(now);
        final LocalDateTime localNow = LocalDateTime.now();
        Mockito.when(temporalHelper.toDatabaseTime(now)).thenReturn(localNow);

        Mockito.when(indexHelper.canIndex(mimeType)).thenReturn(true);

        final DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setUuid(documentUuid.toString());
        documentEntity.setTitle(title);
        documentEntity.setContentId(contentId);
        documentEntity.setCreated(localNow);
        documentEntity.setModified(localNow);
        documentEntity.setContentModified(localNow);
        documentEntity.setContentIndexStatus(ContentIndexStatus.WAITING);

        final Long documentId = 2L;
        final DocumentEntity createdDocumentEntity = new DocumentEntity();
        createdDocumentEntity.setId(documentId);

        Mockito.when(documentRepository.save(documentEntity)).thenReturn(Mono.just(createdDocumentEntity));

        final DocumentAndContentEntities entities = new DocumentAndContentEntities(createdDocumentEntity, contentEntity);
        Mockito.when(indexHelper.indexDocument(entities))
                .thenReturn(Mono.just(entities));

        final Document document = new Document(
                documentUuid,
                now,
                now,
                now,
                contentUuid,
                mimeType,
                title
        );
        Mockito.when(documentMapper.map(entities)).thenReturn(document);

        StepVerifier.create(documentService.create(title, contentUuid))
                .expectNext(document)
                .expectComplete()
                .verify();
    }


    @Test
    public void testCreateDocumentNotSupportedInIndex() {
        final String title = "title";
        final UUID contentUuid = UUID.randomUUID();
        final Long contentId = 1L;
        final String mimeType = MediaType.TEXT_PLAIN_VALUE;
        final ContentEntity contentEntity = new ContentEntity();
        contentEntity.setUuid(contentUuid.toString());
        contentEntity.setId(contentId);
        contentEntity.setMimeType(mimeType);
        Mockito.when(contentRepository.findByUuid(contentUuid.toString())).thenReturn(Mono.just(contentEntity));

        final UUID documentUuid = UUID.randomUUID();
        Mockito.when(uuidHelper.createUuid()).thenReturn(documentUuid);

        final ZonedDateTime now = ZonedDateTime.now();
        Mockito.when(temporalHelper.now()).thenReturn(now);
        final LocalDateTime localNow = LocalDateTime.now();
        Mockito.when(temporalHelper.toDatabaseTime(now)).thenReturn(localNow);

        Mockito.when(indexHelper.canIndex(mimeType)).thenReturn(false);

        final DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setUuid(documentUuid.toString());
        documentEntity.setTitle(title);
        documentEntity.setContentId(contentId);
        documentEntity.setCreated(localNow);
        documentEntity.setModified(localNow);
        documentEntity.setContentModified(localNow);
        documentEntity.setContentIndexStatus(ContentIndexStatus.UNSUPPORTED);

        final Long documentId = 2L;
        final DocumentEntity createdDocumentEntity = new DocumentEntity();
        createdDocumentEntity.setId(documentId);

        Mockito.when(documentRepository.save(documentEntity)).thenReturn(Mono.just(createdDocumentEntity));

        final DocumentAndContentEntities entities = new DocumentAndContentEntities(createdDocumentEntity, contentEntity);
        Mockito.when(indexHelper.indexDocument(entities))
                .thenReturn(Mono.just(entities));

        final Document document = new Document(
                documentUuid,
                now,
                now,
                now,
                contentUuid,
                mimeType,
                title
        );
        Mockito.when(documentMapper.map(entities)).thenReturn(document);

        StepVerifier.create(documentService.create(title, contentUuid))
                .expectNext(document)
                .expectComplete()
                .verify();
    }


}