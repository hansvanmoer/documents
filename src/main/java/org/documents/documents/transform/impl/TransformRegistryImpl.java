package org.documents.documents.transform.impl;

import org.documents.documents.model.transform.TransformType;
import org.documents.documents.transform.Transform;
import org.documents.documents.transform.TransformRegistry;

import java.util.*;

public class TransformRegistryImpl implements TransformRegistry {

    private final Map<TransformType, Transform> transforms;

    public TransformRegistryImpl(Collection<Transform> transforms) {
        final HashMap<TransformType, Transform> mapping = new HashMap<>();
        transforms.forEach(transform -> transform.getSupportedTransformTypes().forEach(type -> mapping.put(type, transform)));
        this.transforms = Collections.unmodifiableMap(mapping);
    }

    @Override
    public Optional<Transform> getTransform(String sourceMimeType, String targetMimeType) {
        return Optional.ofNullable(transforms.get(new TransformType(sourceMimeType, targetMimeType)));
    }
}
