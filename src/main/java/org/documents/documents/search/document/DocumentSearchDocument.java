package org.documents.documents.search.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * Somewhat weird name, but in elastic entities are called "documents"
 */
@Data
@Document(indexName = "document")
public class DocumentSearchDocument {
    @Id
    private String uuid;

    @Field(type = FieldType.Date)
    private LocalDateTime created;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;
}
