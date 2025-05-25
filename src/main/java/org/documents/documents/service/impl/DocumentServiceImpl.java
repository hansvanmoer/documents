package org.documents.documents.service.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.helper.UuidHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.model.api.ContentIndexStatus;
import org.documents.documents.model.exception.NotFoundException;
import org.documents.documents.model.api.Document;
import org.documents.documents.db.repository.ContentRepository;
import org.documents.documents.db.repository.DocumentRepository;
import org.documents.documents.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private final ContentRepository contentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final IndexHelper indexHelper;
    private final RenditionHelper renditionHelper;
    private final TemporalHelper temporalHelper;
    private final UuidHelper uuidHelper;

    @Override
    public Mono<Document> create(UUID contentUuid) {
        final Mono<ContentEntity> contentProducer = contentRepository.findByUuid(contentUuid.toString())
                .switchIfEmpty(Mono.error(new NotFoundException("content for new document not found", contentUuid)));
        return contentProducer
                .flatMap(this::create)
                .flatMap(t -> indexHelper.indexDocumentOrScheduleIndexation(t.getT1(), t.getT2()).map(d -> Tuples.of(d, t.getT2())))
                .map(t -> documentMapper.map(t.getT1(), t.getT2()));
    }

    private Mono<Tuple2<DocumentEntity, ContentEntity>> create(ContentEntity contentEntity) {
        final ZonedDateTime now = temporalHelper.now();
        final UUID uuid = uuidHelper.createUuid();
        final DocumentEntity entity = new DocumentEntity();
        entity.setUuid(uuid.toString());
        entity.setCreated(temporalHelper.toDatabaseTime(now));
        entity.setContentId(contentEntity.getId());
        entity.setContentIndexStatus(isIndexSupported(contentEntity) ? ContentIndexStatus.WAITING : ContentIndexStatus.UNSUPPORTED);
        return documentRepository.save(entity)
                .map(saved -> Tuples.of(saved, contentEntity));
    }

    private boolean isIndexSupported(ContentEntity contentEntity) {
        return renditionHelper.isSupported(contentEntity.getMimeType(), MediaType.TEXT_PLAIN_VALUE);
    }
}
