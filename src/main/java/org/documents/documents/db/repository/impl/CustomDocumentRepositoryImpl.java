package org.documents.documents.db.repository.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.db.model.DocumentWithContentEntity;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.documents.documents.model.api.ContentIndexStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
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
            "d.title as title " +
            "FROM document d, content c " +
            "WHERE " +
            "d.content_id = c.id";

    private static final String FIND_SQL = BASE_QUERY + " OFFSET :offset LIMIT :limit";

    private static final String FIND_BY_UUID_SQL = BASE_QUERY + " WHERE uuid = :uuid";

    private final DatabaseClient databaseClient;

    @Override
    public Mono<DocumentWithContentEntity> findByUuid(UUID uuid) {
        return databaseClient.sql(FIND_BY_UUID_SQL)
                .bind("uuid", uuid.toString())
                .fetch()
                .one()
                .map(this::map);
    }

    @Override
    public Flux<DocumentWithContentEntity> find(Pageable pageable) {
        return databaseClient.sql(FIND_SQL)
                .bind("offset",pageable.getOffset())
                .bind("limit",pageable.getPageSize())
                .fetch()
                .all()
                .map(this::map);
    }

    private DocumentWithContentEntity map(Map<String, Object> row) {
        final DocumentWithContentEntity entity = new DocumentWithContentEntity();
        entity.setId((Long)row.get("id"));
        entity.setUuid((String)row.get("uuid"));
        entity.setMimeType((String)row.get("mimeType"));
        entity.setCreated((LocalDateTime) row.get("created"));
        entity.setContentIndexStatus(ContentIndexStatus.valueOf((String)row.get("contentIndexStatus")));
        entity.setTitle((String)row.get("title"));
        return entity;
    }
}
