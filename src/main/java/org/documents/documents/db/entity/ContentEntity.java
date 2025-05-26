package org.documents.documents.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table(schema = "public", name = "content")
public class ContentEntity {
    @Id
    private Long id;
    private String uuid;
    private LocalDateTime created;
    @Column("mime_type")
    private String mimeType;
}
