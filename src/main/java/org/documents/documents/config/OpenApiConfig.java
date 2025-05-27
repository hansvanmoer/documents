package org.documents.documents.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Document store",
        version = "0.0.1",
        description = "A proof of concept document store",
        contact = @Contact(name = "niemand", email = "bestaatniet@gmail.com", url = "unknown.host"),
        license = @License(name = "GPL version 3", url = "https://www.gnu.org/licenses/gpl-3.0.en.html"),
        summary = "A simple API to show document storage, transformation, previews and indexation"),
        servers = @Server(url = "http://localhost:8080", description = "Local server")
)
public class OpenApiConfig {}
