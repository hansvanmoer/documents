package org.documents.documents.helper;

import org.documents.documents.file.FileReference;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.util.UUID;

public interface RequestTransformHelper {

    void requestTransform(UUID contentUuid, FileReference file, String sourceMimeType, String targetMimeType);

}
