package org.documents.documents.search.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

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

    @WriteOnlyProperty
    @Field(type = FieldType.Text)
    private String content;
}
