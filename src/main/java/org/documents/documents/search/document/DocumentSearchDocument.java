package org.documents.documents.search.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.ZonedDateTime;

/**
 * Somewhat weird name, but in elastic entities are called "documents"
 */
@Data
@Document(indexName = "document")
public class DocumentSearchDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String uuid;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private ZonedDateTime created;

    @Field(type = FieldType.Keyword)
    private String mimeType;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;
}
