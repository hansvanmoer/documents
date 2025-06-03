package org.documents.documents.config.settings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "documents.api")
@Getter
@Setter
public class ApiSettings {
    private int defaultPageSize;
    private Set<Locale> supportedLocales;
}
