package org.documents.documents.mapper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.helper.TemporalHelper;
import org.documents.documents.mapper.ContentMapper;
import org.documents.documents.model.api.Content;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class ContentMapperImpl implements ContentMapper {

    private final TemporalHelper temporalHelper;

    @Override
    public Content map(ContentEntity input) {
        return new Content(
                UUID.fromString(input.getUuid()),
                temporalHelper.fromDatabaseTime(input.getCreated()),
                input.getMimeType()
        );
    }
}
