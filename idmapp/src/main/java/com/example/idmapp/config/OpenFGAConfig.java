package com.example.idmapp.config;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.configuration.ClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFGAConfig {

    @Value("${openfga.api-url}")
    private String apiUrl;

    @Value("${openfga.store-id}")
    private String storeId;

    @Value("${openfga.api-token}")
    private String apiToken;

    @Bean
    public OpenFgaClient openFgaClient() {
        ClientConfiguration config = new ClientConfiguration()
            .apiUrl(apiUrl)
            .storeId(storeId)
            .apiToken(apiToken);

        return new OpenFgaClient(config);
    }
} 