package org.documents.documents.search.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.search.repository.CustomDocumentSearchRepository;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Repository
@Slf4j
public class CustomDocumentSearchRepositoryImpl implements CustomDocumentSearchRepository {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    @Override
    public Mono<Void> updateDocumentMetadata(Long id, String title) {
        final Document document = Document.create();
        document.put("title", title);
        final UpdateQuery query = UpdateQuery.builder(id.toString())
                        .withDocument(document)
                        .build();
        return reactiveElasticsearchTemplate.update(query, IndexCoordinates.of("document"))
                .map(response -> response.getResult().name()).map(name -> {
                    log.debug(name);
                    return name;
                }).then();
    }
}
