package org.documents.documents.transform.impl;

import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.model.transform.TransformType;
import org.documents.documents.transform.Transform;

import java.util.*;
import java.util.stream.Collectors;

public class CompositeTransform implements Transform {
    private final Transform first;
    private final Transform second;
    private final String intermediateMimeType;
    private final Set<TransformType> supportedTransformTypes;
    private final List<String> preferredSourceMimeTypes;

    public CompositeTransform(Transform first, String intermediateMimeType, Transform second) {
        this.first = first;
        this.second = second;
        this.intermediateMimeType = intermediateMimeType;
        final Set<TransformType> types = new HashSet<>();
        final Set<String> inputTypes = first.getSupportedTransformTypes().stream().map(TransformType::sourceMimeType).collect(Collectors.toSet());
        final Set<String> outputTypes = second.getSupportedTransformTypes().stream().map(TransformType::targetMimeType).collect(Collectors.toSet());
        for(String inputType : inputTypes) {
            for(String outputType : outputTypes) {
                types.add(new TransformType(inputType, outputType));
            }
        }
        this.supportedTransformTypes = Collections.unmodifiableSet(types);
        final List<String> preference = new ArrayList<>();
        preference.addAll(second.getPreferredSourceMimeTypes(intermediateMimeType));
        preference.addAll(first.getPreferredSourceMimeTypes(intermediateMimeType));
        this.preferredSourceMimeTypes = Collections.unmodifiableList(preference);
    }

    @Override
    public TransformResult transform(UUID uuid, String sourceMimeType, String targetMimeType) {
        final TransformResult result = first.transform(uuid, sourceMimeType, intermediateMimeType);
        if(result.success()) {
            return second.transform(uuid, intermediateMimeType, targetMimeType);
        } else {
            return result;
        }
    }

    @Override
    public Set<TransformType> getSupportedTransformTypes() {
        return supportedTransformTypes;
    }

    @Override
    public List<String> getPreferredSourceMimeTypes(String targetMimeType) {
        return preferredSourceMimeTypes;
    }

    @Override
    public int getPriority() {
        return first.getPriority() + second.getPriority();
    }
}
