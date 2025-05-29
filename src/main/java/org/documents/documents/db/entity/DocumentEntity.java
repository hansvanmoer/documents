package org.documents.documents.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table(schema = "public", name = "document")
public class DocumentEntity {
    @Id
    private Long id;
    private String uuid;
    private LocalDateTime created;
    private LocalDateTime modified;
    @Column("content_modified")
    private LocalDateTime contentModified;
    @Column("content_id")
    private Long contentId;
    @Column("content_index_status")
    private ContentIndexStatus contentIndexStatus;
    private String title;
}