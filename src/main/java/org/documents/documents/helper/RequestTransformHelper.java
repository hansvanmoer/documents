package org.documents.documents.helper;

import org.springframework.http.MediaType;

import java.nio.file.Path;

public interface RequestTransformHelper {

    void requestTransform(Path path, MediaType sourceType, MediaType targetType);

}
