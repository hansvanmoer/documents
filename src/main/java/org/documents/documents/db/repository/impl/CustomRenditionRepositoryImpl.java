package org.documents.documents.db.repository.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.db.mapper.RowMapper;
import org.documents.documents.db.mapper.impl.RowMapperImpl;
import org.documents.documents.db.repository.CustomRenditionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Repository
public class CustomRenditionRepositoryImpl implements CustomRenditionRepository {
    private static final String FIND_ALL =
            "SELECT " +
                    "id, " +
                    "uuid, " +
                    "created, " +
                    "mime_type," +
                    "content_id "+
                    "FROM rendition " +
                    "LIMIT :limit OFFSET :offset";

    private final DatabaseClient databaseClient;
    private final RowMapper<RenditionEntity> renditionEntityRowMapper = RowMapperImpl.builder(RenditionEntity::new)
        .property("id", RenditionEntity::setId)
        .property("uuid", RenditionEntity::setUuid)
        .property("mime_type", RenditionEntity::setMimeType)
        .property("created", RenditionEntity::setCreated)
        .property("content_id", RenditionEntity::setContentId)
        .build();

    @Override
    public Flux<RenditionEntity> findAll(Pageable pageable) {
        return databaseClient.sql(FIND_ALL)
                .bind("limit", pageable.getPageSize())
                .bind("offset", pageable.getOffset())
                .fetch()
                .all()
                .map(renditionEntityRowMapper::map);
    }
}
