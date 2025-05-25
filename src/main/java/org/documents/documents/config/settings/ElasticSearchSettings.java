package org.documents.documents.config.settings;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class ElasticSearchSettings {
    @Value("${spring.elasticsearch.uris}")
    private List<String> uris;
    @Value("${spring.elasticsearch.username}")
    private String userName;
    @Value("${spring.elasticsearch.password}")
    private String password;
}
