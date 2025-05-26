package org.documents.documents.db.repository.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.model.DocumentWithContentEntity;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Repository
public class CustomDocumentRepositoryImpl implements CustomDocumentRepository {

    private static final String FIND_SQL = "SELECT " +
            "d.id as id, " +
            "d.uuid as uuid, " +
            "c.mime_type as mimeType, " +
            "d.content_index_status as contentIndexStatus, " +
            "d.created as created, " +
            "d.title as title" +
            "FROM document d, content c " +
            "WHERE " +
            "d.content_id = c.id" +
            "OFFSET %s LIMIT %s";

    private final DatabaseClient databaseClient;

    @Override
    public Flux<DocumentWithContentEntity> find(Pageable pageable) {
        final String query = String.format(FIND_SQL, pageable.getOffset(), pageable.getPageSize());
        return databaseClient.sql(query).fetch().all().ofType(DocumentWithContentEntity.class);
    }


}
