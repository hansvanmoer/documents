package org.documents.documents.helper;

import java.util.UUID;

public interface RequestTransformHelper {

    void requestTransform(UUID contentUuid, String targetMimeType);

}
