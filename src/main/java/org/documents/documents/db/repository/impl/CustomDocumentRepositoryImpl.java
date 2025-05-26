package org.documents.documents.db.repository.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.model.DocumentWithContentEntity;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Repository
public class CustomDocumentRepositoryImpl implements CustomDocumentRepository {

    private static final String BASE_QUERY =
            "SELECT " +
            "d.id as id, " +
            "d.uuid as uuid, " +
            "c.mime_type as mimeType, " +
            "d.content_index_status as contentIndexStatus, " +
            "d.created as created, " +
            "d.title as title" +
            "FROM document d, content c " +
            "WHERE " +
            "d.content_id = c.id";

    private static final String FIND_SQL = BASE_QUERY + " OFFSET ?1 LIMIT ?2";

    private static final String FIND_BY_UUID_SQL = BASE_QUERY + " WHERE uuid = ?1";

    private final DatabaseClient databaseClient;

    @Override
    public Mono<DocumentWithContentEntity> findByUuid(UUID uuid) {
        return databaseClient.sql(FIND_BY_UUID_SQL)
                .bind(0, uuid.toString())
                .fetch()
                .one()
                .ofType(DocumentWithContentEntity.class);
    }

    @Override
    public Flux<DocumentWithContentEntity> find(Pageable pageable) {
        return databaseClient.sql(FIND_SQL)
                .bind(0,pageable.getOffset())
                .bind(1,pageable.getPageSize())
                .fetch()
                .all()
                .ofType(DocumentWithContentEntity.class);
    }
}
