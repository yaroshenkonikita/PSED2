package com.psed2.rateprinter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.psed2.rateprinter.rpc.JsonRpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

@Component
public class RestProviderRateClient implements ProviderRateClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestProviderRateClient.class);

    private final RestTemplate restTemplate;
    private final RateRequestFactory requestFactory;
    private final JsonRpcRateResponseParser responseParser;
    private final String providerUrl;
    private final String clientName;

    public RestProviderRateClient(
            RestTemplate restTemplate,
            RateRequestFactory requestFactory,
            JsonRpcRateResponseParser responseParser,
            @Value("${provider.url:http://currency-rate-provider/api/v1/rpc}") String providerUrl,
            @Value("${client.name:rate-printer}") String clientName
    ) {
        this.restTemplate = restTemplate;
        this.requestFactory = requestFactory;
        this.responseParser = responseParser;
        this.providerUrl = providerUrl;
        this.clientName = clientName;
    }

    @Override
    public Optional<RateSnapshot> getRate(String pair, Instant at) {
        try {
            return fetchRate(pair, at);
        } catch (Exception ex) {
            LOGGER.warn("Cannot fetch rate from provider: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private Optional<RateSnapshot> fetchRate(String pair, Instant at) {
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                providerUrl,
                createHttpEntity(pair, at),
                JsonNode.class
        );
        return responseParser.parse(response.getBody());
    }

    private HttpEntity<JsonRpcRequest> createHttpEntity(String pair, Instant at) {
        return new HttpEntity<>(requestFactory.create(pair, at), jsonHeaders());
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Client-Name", clientName);
        return headers;
    }
}
