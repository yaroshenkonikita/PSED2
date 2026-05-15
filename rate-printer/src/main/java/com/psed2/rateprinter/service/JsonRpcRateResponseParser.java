package com.psed2.rateprinter.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JsonRpcRateResponseParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcRateResponseParser.class);

    public Optional<RateSnapshot> parse(JsonNode body) {
        if (body == null) {
            LOGGER.warn("RPC error: empty response body");
            return Optional.empty();
        }

        if (hasError(body)) {
            logError(body.get("error"));
            return Optional.empty();
        }

        JsonNode result = body.path("result");
        if (isRateMissing(result)) {
            LOGGER.warn("RPC error: rate is missing in response");
            return Optional.empty();
        }

        return Optional.of(toSnapshot(result));
    }

    private boolean hasError(JsonNode body) {
        JsonNode error = body.get("error");
        return error != null && !error.isNull();
    }

    private void logError(JsonNode error) {
        LOGGER.warn(
                "RPC error {}: {}",
                error.path("code").asInt(),
                error.path("message").asText("unknown")
        );
    }

    private boolean isRateMissing(JsonNode result) {
        return Double.isNaN(result.path("rate").asDouble(Double.NaN));
    }

    private RateSnapshot toSnapshot(JsonNode result) {
        return new RateSnapshot(
                result.path("pair").asText("USD/RUB"),
                result.path("rate").asDouble(),
                result.path("at").asText("n/a")
        );
    }
}
