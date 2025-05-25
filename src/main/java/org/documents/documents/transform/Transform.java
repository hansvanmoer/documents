package org.documents.documents.transform;

import org.documents.documents.model.transform.TransformResult;
import org.documents.documents.model.transform.TransformType;

import java.nio.file.Path;
import java.util.Set;

public interface Transform {

    TransformResult transform(Path sourcePath, String sourceMimeType, String targetMimeType, String targetExtension);

    Set<TransformType> getSupportedTransformTypes();
}
