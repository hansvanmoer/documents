package org.documents.documents.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DocumentUpdate {
    private final String title;
    private final UUID contentUuid;
}
