package org.documents.documents.helper;

import org.documents.documents.file.FileReference;

import java.nio.file.Path;
import java.util.UUID;

public interface RunTransformHelper {

    void runTransform(UUID contentUuid, FileReference file, String sourceMimeType, String targetMimeType);

}
