package org.documents.documents;

import org.documents.documents.config.settings.Settings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = Settings.class)
@SpringBootApplication
public class DocumentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentsApplication.class, args);
	}

}
