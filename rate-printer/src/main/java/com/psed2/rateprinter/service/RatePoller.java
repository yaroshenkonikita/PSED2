package com.psed2.rateprinter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.psed2.rateprinter.rpc.JsonRpcRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
public class RatePoller {
    private final RestTemplate restTemplate;
    private final String providerUrl;

    public RatePoller(
            RestTemplate restTemplate,
            @Value("${provider.url:http://currency-rate-provider/rpc}") String providerUrl
    ) {
        this.restTemplate = restTemplate;
        this.providerUrl = providerUrl;
    }

    @Scheduled(
            fixedDelayString = "${poll.interval.ms:5000}",
            initialDelayString = "${poll.initial-delay.ms:1000}"
    )
    public void printRate() {
        JsonRpcRequest request = new JsonRpcRequest("2.0", "getUsdRubRate", null, 1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                    providerUrl,
                    new HttpEntity<>(request, headers),
                    JsonNode.class
            );

            JsonNode body = response.getBody();
            if (body == null) {
                System.err.println("RPC error: empty response body");
                return;
            }

            JsonNode error = body.get("error");
            if (error != null && !error.isNull()) {
                System.err.printf(
                        "[%s] RPC error %d: %s%n",
                        Instant.now(),
                        error.path("code").asInt(),
                        error.path("message").asText("unknown")
                );
                return;
            }

            JsonNode result = body.path("result");
            double rate = result.path("rate").asDouble(Double.NaN);
            String at = result.path("at").asText("n/a");
            String pair = result.path("pair").asText("USD/RUB");

            if (Double.isNaN(rate)) {
                System.err.println("RPC error: rate is missing in response");
                return;
            }

            System.out.printf("[%s] %s = %.4f%n", at, pair, rate);
        } catch (Exception ex) {
            System.err.printf("[%s] Cannot fetch rate: %s%n", Instant.now(), ex.getMessage());
        }
    }
}
