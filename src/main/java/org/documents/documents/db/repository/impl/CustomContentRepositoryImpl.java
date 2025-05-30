package org.documents.documents.db.repository.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.mapper.RowMapper;
import org.documents.documents.db.mapper.impl.RowMapperImpl;
import org.documents.documents.db.repository.CustomContentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Repository
public class CustomContentRepositoryImpl implements CustomContentRepository {

    private static final String FIND_SQL =
        "SELECT " +
        "id, " +
        "uuid, " +
        "created, " +
        "mime_type " +
        "FROM content " +
        "OFFSET :offset LIMIT :limit";

    private final RowMapper<ContentEntity> mapper = RowMapperImpl.builder(ContentEntity::new)
            .property("id", ContentEntity::setId)
            .property("uuid", ContentEntity::setUuid)
            .property("created", ContentEntity::setCreated)
            .property("mime_type", ContentEntity::setMimeType)
            .build();

    private final DatabaseClient databaseClient;

    @Override
    public Flux<ContentEntity> findAll(Pageable pageable) {
        return databaseClient.sql(FIND_SQL)
                .bind("offset",pageable.getOffset())
                .bind("limit",pageable.getPageSize())
                .fetch()
                .all()
                .map(mapper::map);
    }
}
