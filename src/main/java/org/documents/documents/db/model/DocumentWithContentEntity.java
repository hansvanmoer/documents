package org.documents.documents.db.model;

import lombok.Data;
import org.documents.documents.model.api.ContentIndexStatus;

import java.time.LocalDateTime;

@Data
public class DocumentWithContentEntity {
    private Long id;
    private String uuid;
    private String mimeType;
    private ContentIndexStatus contentIndexStatus;
    private LocalDateTime created;
    private String title;
}