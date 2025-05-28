package org.documents.documents.model;

import lombok.Getter;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.RenditionEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ContentAndRenditionEntities {
    private final ContentEntity contentEntity;
    private final Map<String, RenditionEntity> renditionEntitiesByMimeType;
    
    public ContentAndRenditionEntities(ContentEntity contentEntity, List<RenditionEntity> renditionEntities) {
        this.contentEntity = contentEntity;
        final Map<String, RenditionEntity> map = new HashMap<>(renditionEntities.size());
        renditionEntities.forEach(renditionEntity -> map.put(renditionEntity.getMimeType(), renditionEntity));
        this.renditionEntitiesByMimeType = Collections.unmodifiableMap(map);
    }
}
