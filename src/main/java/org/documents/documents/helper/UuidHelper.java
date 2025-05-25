package org.documents.documents.helper;

import java.util.UUID;

public interface UuidHelper {

    UUID NULL_UUID = new UUID(0L, 0L);

    UUID createUuid();
}
