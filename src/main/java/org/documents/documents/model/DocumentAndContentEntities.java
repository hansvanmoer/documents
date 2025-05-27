package org.documents.documents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.documents.documents.db.entity.ContentEntity;
import org.documents.documents.db.entity.DocumentEntity;

@AllArgsConstructor
@Getter
public class DocumentAndContentEntities {
    private final DocumentEntity documentEntity;
    private final ContentEntity contentEntity;
    private final String contentAsText;

    public DocumentAndContentEntities withContentAsText(String contentAsText) {
        return new DocumentAndContentEntities(documentEntity, contentEntity, contentAsText);
    }
}
