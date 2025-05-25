package org.documents.documents.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface TemporalHelper {
    ZonedDateTime now();

    ZonedDateTime fromDatabaseTime(LocalDateTime databaseTime);

    LocalDateTime toDatabaseTime(ZonedDateTime time);
}
