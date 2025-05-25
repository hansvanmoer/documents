package org.documents.documents.config;

import com.rabbitmq.client.impl.CredentialsProvider;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class SearchConfig extends ReactiveElasticsearchConfiguration {

    /*
     * Warning, we disable CA certificate verification for test purposes
     */
    @Override
    public ClientConfiguration clientConfiguration() {
        try {
            final SSLContextBuilder sslBuilder = SSLContexts.custom()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true);
            final SSLContext sslContext = sslBuilder.build();
            return ClientConfiguration.builder()
                    .connectedTo("localhost:9200")
                    .withBasicAuth("elastic", "changeme") //add your username and password
                    .build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
