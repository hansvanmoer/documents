package org.documents.documents.helper.impl;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.TemporalSettings;
import org.documents.documents.helper.TemporalHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Component
public class TemporalHelperImpl implements TemporalHelper {

    private final TemporalSettings temporalSettings;

    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    @Override
    public ZonedDateTime fromDatabaseTime(LocalDateTime databaseTime) {
        return databaseTime.atZone(temporalSettings.getZoneId());
    }

    @Override
    public LocalDateTime toDatabaseTime(ZonedDateTime time) {
        return time.withZoneSameInstant(temporalSettings.getZoneId()).toLocalDateTime();
    }
}
