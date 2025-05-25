package org.documents.documents.model.transform;

import lombok.Getter;

import java.nio.file.Path;
import java.util.UUID;

@Getter
public class TransformResult {
    private final Path resultPath;
    private final String message;

    public TransformResult(Path resultPath) {
        this.resultPath = resultPath;
        this.message = null;
    }

    public TransformResult(String message) {
        this.resultPath = null;
        this.message = message;
    }

    public boolean isSuccess() {
        return resultPath != null;
    }
}
