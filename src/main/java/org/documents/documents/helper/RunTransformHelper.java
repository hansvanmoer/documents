package org.documents.documents.helper;

import java.nio.file.Path;

public interface RunTransformHelper {

    void runTransform(Path path, String sourceMimeType, String targetMimeType);

}
