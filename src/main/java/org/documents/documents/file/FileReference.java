package org.documents.documents.file;

import java.util.UUID;

public record FileReference (FileStoreType fileStoreType, UUID uuid) {}