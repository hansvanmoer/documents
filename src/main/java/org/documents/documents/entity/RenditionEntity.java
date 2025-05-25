package org.documents.documents.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(schema = "public", name = "rendition")
@Data
public class RenditionEntity {
    @Id
    private Long id;
    @Column("original_id")
    private Long originalId;
    @Column("rendition_id")
    private Long renditionId;
}
