package org.documents.documents.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.mapper.RenditionMapper;
import org.documents.documents.model.api.Rendition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class RenditionMapperImpl implements RenditionMapper {

    private final TemporalHelper temporalHelper;

    @Override
    public Rendition map(RenditionEntity renditionEntity) {
        return new Rendition(
                UUID.fromString(renditionEntity.getUuid()),
                renditionEntity.getMimeType(),
                temporalHelper.fromDatabaseTime(renditionEntity.getCreated())
        );
    }
}
