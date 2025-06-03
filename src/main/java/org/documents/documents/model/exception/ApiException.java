package org.documents.documents.model.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.documents.documents.model.api.ErrorResponse;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Slf4j
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;

    public ApiException(ErrorCode errorCode, Object ... args) {
        super(createMessage(errorCode, args).orElse(""));
        this.errorCode = errorCode;
        this.args = args;
    }

    public ApiException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(createMessage(errorCode, args).orElse(""), cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    private static Optional<String> createMessage(ErrorCode errorCode, Object... args) {
        try {
            final ResourceBundle messages = ResourceBundle.getBundle("error");
            return Optional.of(String.format(messages.getString(errorCode.toString()), args));
        } catch(MissingResourceException e) {
            log.error("error resource bundle is missing value for key '{}'", errorCode);
            return Optional.empty();
        }
    }

    private Map<Locale, String> createMessages(Set<Locale> supportedLocales) {
        final Map<Locale, String> messages = new HashMap<>(supportedLocales.size());
        for(Locale locale : supportedLocales) {
            final ResourceBundle messageBundle = ResourceBundle.getBundle("error", locale);
            try {
                messages.put(locale, String.format(messageBundle.getString(errorCode.toString()), args));
            } catch(MissingResourceException e) {
                log.error("error resource bundle is missing value for key '{}' and locale {}", errorCode, locale);
            }
        }
        return messages;
    }

    private List<String> createArgList() {
        if(args == null) {
            return Collections.emptyList();
        } else {
            return Stream.of(args).map(Object::toString).collect(Collectors.toList());
        }
    }

    public ResponseEntity<ErrorResponse> toResponseEntity(Set<Locale> supportedLocales) {
        return ResponseEntity.status(getErrorCode().getHttpStatusCode())
                .body(new ErrorResponse(
                        getErrorCode(),
                        createArgList(),
                        getMessage(),
                        createMessages(supportedLocales)
                ));
    }
}
