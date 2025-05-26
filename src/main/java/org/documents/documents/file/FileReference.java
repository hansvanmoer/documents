package org.documents.documents.file;

import lombok.ToString;

import java.util.UUID;

public record FileReference (FileStoreType fileStoreType, UUID uuid) {}