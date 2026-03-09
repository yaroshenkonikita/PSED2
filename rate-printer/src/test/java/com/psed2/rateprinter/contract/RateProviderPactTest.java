package com.psed2.rateprinter.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "currency-rate-provider", pactVersion = PactSpecVersion.V3)
class RateProviderPactTest {

    @Pact(consumer = "rate-printer")
    RequestResponsePact getRatePact(PactDslWithProvider builder) {
        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringValue("jsonrpc", "2.0")
                .stringValue("method", "getRate");
        PactDslJsonBody paramsBody = requestBody.object("params");
        paramsBody.stringValue("pair", "USD/RUB");
        paramsBody.stringValue("at", "2026-03-10T08:00:00Z");
        paramsBody.closeObject();
        requestBody.integerType("id", 1);

        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringValue("jsonrpc", "2.0");
        PactDslJsonBody resultBody = responseBody.object("result");
        resultBody.stringValue("pair", "USD/RUB");
        resultBody.numberType("rate", 90.1234);
        resultBody.stringMatcher("at", ".+", "2026-01-01T00:00:00Z");
        resultBody.closeObject();
        responseBody.integerType("id", 1);

        return builder
                .given("rate for pair and time exists")
                .uponReceiving("JSON-RPC request getRate with pair and at")
                .path("/api/v1/rpc")
                .method("POST")
                .headers(Map.of("Content-Type", "application/json"))
                .body(requestBody)
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(responseBody)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getRatePact")
    void shouldReceiveExpectedRateResponse(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
                {"jsonrpc":"2.0","method":"getRate","params":{"pair":"USD/RUB","at":"2026-03-10T08:00:00Z"},"id":1}
                """;

        JsonNode response = restTemplate.postForObject(
                mockServer.getUrl() + "/api/v1/rpc",
                new HttpEntity<>(requestBody, headers),
                JsonNode.class
        );

        assertNotNull(response);
        assertEquals("2.0", response.path("jsonrpc").asText());
        assertEquals("USD/RUB", response.path("result").path("pair").asText());
    }
}
