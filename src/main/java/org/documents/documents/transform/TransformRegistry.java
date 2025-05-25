package org.documents.documents.transform;

import java.util.Optional;

public interface TransformRegistry {
    Optional<Transform> getTransform(String sourceMimeType, String targetMimeType);
}
