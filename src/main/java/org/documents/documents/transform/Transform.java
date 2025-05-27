package org.documents.documents.transform;

import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.model.transform.TransformType;

import java.util.Set;
import java.util.UUID;

public interface Transform {

    TransformResult transform(UUID uuid, String sourceMimeType, String targetMimeType);

    Set<TransformType> getSupportedTransformTypes();
}
