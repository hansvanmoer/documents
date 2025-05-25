package org.documents.documents.config;

import lombok.AllArgsConstructor;
import org.documents.documents.config.settings.ElasticSearchSettings;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;

import java.net.URI;
import java.net.URISyntaxException;


@AllArgsConstructor
@Configuration
public class SearchConfig extends ReactiveElasticsearchConfiguration {

    private final ElasticSearchSettings searchSettings;

    /*
     * Warning, we disable CA certificate verification for test purposes
     */
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(searchSettings.getUris().stream().map(this::normalizeUri).toArray(String[]::new))
                .withBasicAuth(searchSettings.getUserName(), searchSettings.getPassword()) //add your username and password
                .build();
    }

    private String normalizeUri(String value) {
        try {
            final URI uri = new URI(value);
            return uri.getHost() + ":" + uri.getPort();
        } catch(URISyntaxException e) {
            throw new IllegalArgumentException("invalid elastic url's", e);
        }
    }
}
