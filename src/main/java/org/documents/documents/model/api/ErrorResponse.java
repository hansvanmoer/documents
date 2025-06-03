package org.documents.documents.model.api;

import org.documents.documents.model.exception.ErrorCode;

import java.util.List;

public record ErrorResponse(ErrorCode code, String message, List<String> args) {}
