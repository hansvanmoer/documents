package org.documents.documents.model.transform;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Getter
public class TransformResult {
    private final Set<String> outputMimeTypes;
    private final String errorMessage;

    public TransformResult(Set<String> outputMimeTypes) {
        this.outputMimeTypes = Collections.unmodifiableSet(outputMimeTypes);
        this.errorMessage = null;
    }

    public TransformResult(String errorMessage) {
        this.outputMimeTypes = Collections.emptySet();
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return !outputMimeTypes.isEmpty();
    }

    public TransformResult andThen(Supplier<TransformResult> second) {
        if(isSuccess()) {
            final TransformResult other = second.get();
            if(other.isSuccess()) {
                final Set<String> finalOutputMimeTypes = new HashSet<>(this.outputMimeTypes);
                finalOutputMimeTypes.addAll(other.outputMimeTypes);
                return new TransformResult(finalOutputMimeTypes);
            } else {
                return other;
            }
        } else {
            return this;
        }
    }
}