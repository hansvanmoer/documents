package org.documents.documents.helper.impl;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.documents.documents.helper.MimeTypeHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MimeTypeHelperImpl implements MimeTypeHelper {

    private final MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();

    @Override
    public Optional<String> getExtension(String mimeType) {
        try {
            return Optional.ofNullable(mimeTypes.getRegisteredMimeType(mimeType)).map(MimeType::getExtension);
        } catch (MimeTypeException e) {
            return Optional.empty();
        }
    }
}
