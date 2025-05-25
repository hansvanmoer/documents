package org.documents.documents.helper.impl;

import org.documents.documents.helper.UuidHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidHelperImpl implements UuidHelper {
    @Override
    public UUID createUuid() {
        return UUID.randomUUID();
    }
}
