package org.documents.documents.db.repository.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.db.repository.CustomRenditionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Repository
public class CustomRenditionRepositoryImpl implements CustomRenditionRepository {
    private static final String FIND_ALL =
            "SELECT " +
                    "r.id as id, " +
                    "r.uuid as uuid, " +
                    "r.created as created " +
                    "r.mime_type as mimeType, " +
                    "r.content_id as ContentId "+
                    "LIMIT :limit OFFSET :offset";

    private final DatabaseClient databaseClient;

    @Override
    public Flux<RenditionEntity> findAll(Pageable pageable) {
        return databaseClient.sql(FIND_ALL)
                .bind("limit", pageable.getPageSize())
                .bind("offset", pageable.getOffset())
                .fetch()
                .all()
                .map(this::map);
    }

    private RenditionEntity map(Map<String, Object> input) {
        final RenditionEntity entity = new RenditionEntity();
        entity.setId((Long)input.get("id"));
        entity.setUuid((String)input.get("uuid"));
        entity.setMimeType((String)input.get("mimeType"));
        entity.setCreated((LocalDateTime) input.get("created"));
        entity.setContentId((Long)input.get("contentId"));
        return entity;
    }
}
