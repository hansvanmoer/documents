package org.documents.documents.entity;

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
    @Column("content_id")
    private Long contentId;
    private String title;
}