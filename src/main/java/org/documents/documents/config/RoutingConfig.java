package org.documents.documents.config;

import lombok.AllArgsConstructor;
import org.documents.documents.handler.ContentHandler;
import org.documents.documents.handler.DocumentHandler;
import org.documents.documents.model.api.ApiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@AllArgsConstructor
@Configuration
public class RoutingConfig {

    private final ContentHandler contentHandler;
    private final DocumentHandler documentHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .PUT(ApiConstants.CONTENT_PATH, contentHandler::upload)
                .PUT(ApiConstants.DOCUMENT_PATH, documentHandler::create)
                .build();
    }
}
