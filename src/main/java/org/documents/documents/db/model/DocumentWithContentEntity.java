package org.documents.documents.db.model;

import lombok.Data;
import org.documents.documents.db.entity.ContentIndexStatus;

import java.time.LocalDateTime;

@Data
public class DocumentWithContentEntity {
    private Long id;
    private String uuid;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime contentModified;
    private String mimeType;
    private String title;
    private ContentIndexStatus contentIndexStatus;
}