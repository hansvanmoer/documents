package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.entity.ContentEntity;
import org.documents.documents.entity.DocumentEntity;
import org.documents.documents.helper.ContentHelper;
import org.documents.documents.helper.IndexHelper;
import org.documents.documents.helper.RenditionHelper;
import org.documents.documents.mapper.DocumentMapper;
import org.documents.documents.search.document.DocumentSearchDocument;
import org.documents.documents.search.repository.DocumentSearchRepository;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class IndexHelperImpl implements IndexHelper {

    private final ContentHelper contentHelper;
    private final DocumentMapper documentMapper;
    private final DocumentSearchRepository documentSearchRepository;
    private final RenditionHelper renditionHelper;

    @Override
    public Mono<Void> indexDocument(DocumentEntity documentEntity, ContentEntity contentEntity) {
        final DocumentSearchDocument document = documentMapper.mapToSearchDocument(documentEntity);

        final Mono<ContentEntity> contentProducer = renditionHelper.getOrRequestRendition(contentEntity, MediaType.TEXT_PLAIN);
        return contentProducer
                .flatMap(rendition -> contentHelper.readContentAsString(rendition))
                .switchIfEmpty(Mono.just(new String()))
                .map(text -> {
                    document.setContent(text);
                    return document;
                })
                .flatMap(d -> documentSearchRepository.save(d, RefreshPolicy.IMMEDIATE))
                .then();
    }
}
