package org.documents.documents.db.repository.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.mapper.RowMapper;
import org.documents.documents.db.mapper.impl.RowMapperImpl;
import org.documents.documents.db.model.DocumentWithContentEntity;
import org.documents.documents.db.repository.CustomDocumentRepository;
import org.documents.documents.db.entity.ContentIndexStatus;
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
            "d.created as created, " +
            "d.modified as modified, " +
            "d.content_modified as contentModified, " +
            "c.mime_type as mimeType, " +
            "d.title as title, " +
            "d.content_index_status as contentIndexStatus " +
            "FROM document d, content c " +
            "WHERE " +
            "d.content_id = c.id";

    private static final String FIND_SQL = BASE_QUERY + " OFFSET :offset LIMIT :limit";

    private static final String FIND_BY_UUID_SQL = BASE_QUERY + " AND d.uuid = :uuid";

    private final RowMapper<DocumentWithContentEntity> documentWithContentEntityRowMapper = RowMapperImpl.builder(DocumentWithContentEntity::new)
            .property("id", DocumentWithContentEntity::setId)
            .property("uuid", DocumentWithContentEntity::setUuid)
            .property("mimeType", DocumentWithContentEntity::setMimeType)
            .property("created", DocumentWithContentEntity::setCreated)
            .property("modified", DocumentWithContentEntity::setModified)
            .property("contentModified", DocumentWithContentEntity::setContentModified)
            .property("contentIndexStatus", DocumentWithContentEntity::setContentIndexStatus, ContentIndexStatus::valueOf)
            .property("title", DocumentWithContentEntity::setTitle)
            .build();

    private final DatabaseClient databaseClient;

    @Override
    public Mono<DocumentWithContentEntity> findByUuid(UUID uuid) {
        return databaseClient.sql(FIND_BY_UUID_SQL)
                .bind("uuid", uuid.toString())
                .fetch()
                .one()
                .map(documentWithContentEntityRowMapper::map);
    }

    @Override
    public Flux<DocumentWithContentEntity> find(Pageable pageable) {
        return databaseClient.sql(FIND_SQL)
                .bind("offset",pageable.getOffset())
                .bind("limit",pageable.getPageSize())
                .fetch()
                .all()
                .map(documentWithContentEntityRowMapper::map);
    }
}
