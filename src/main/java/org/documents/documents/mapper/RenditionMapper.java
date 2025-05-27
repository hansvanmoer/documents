package org.documents.documents.mapper;

import org.documents.documents.db.entity.RenditionEntity;
import org.documents.documents.model.api.Rendition;

public interface RenditionMapper {

    Rendition map(RenditionEntity renditionEntity);
}
