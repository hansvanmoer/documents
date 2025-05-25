package org.documents.documents.helper;

import java.util.Optional;

public interface MimeTypeHelper {

    Optional<String> getExtension(String mimeType);

}
