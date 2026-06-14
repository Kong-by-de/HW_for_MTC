package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${external.api.baseUrl:http://localhost:8080}")
    private String baseUrl;

    @Value("${external.api.connect-timeout:5000}")
    private int connectTimeout;

    @Value("${external.api.read-timeout:10000}")
    private int readTimeout;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
            .baseUrl(baseUrl)
            .defaultHeader("User-Agent", "ResilientGateway/1.0")
            .defaultHeader("Accept", "application/json")
            .requestFactory(createRequestFactory())
            .build();
    }

    private ClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
}