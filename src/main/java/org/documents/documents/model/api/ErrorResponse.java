package org.documents.documents.model.api;

import org.documents.documents.model.exception.ErrorCode;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public record ErrorResponse(ErrorCode code, List<String> args, String defaultMessage, Map<Locale, String> localizedMessages) {}
