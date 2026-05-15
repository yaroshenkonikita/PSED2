package com.psed2.currencyrateprovider.service.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Component
public class RateQueryExtractor {
    private static final String PARAM_PAIR = "pair";
    private static final String PARAM_AT = "at";

    public RateQuery extract(JsonNode params) {
        validateParams(params);
        String pair = extractRequiredText(params, PARAM_PAIR, "params.pair is required");
        String atRaw = extractRequiredText(params, PARAM_AT, "params.at is required and must be ISO-8601");
        return new RateQuery(pair, parseInstant(atRaw));
    }

    private void validateParams(JsonNode params) {
        if (params == null || params.isNull() || !params.isObject()) {
            throw new IllegalArgumentException("params must be an object with pair and at");
        }
    }

    private String extractRequiredText(JsonNode params, String field, String errorMessage) {
        String value = params.path(field).asText(null);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    private Instant parseInstant(String rawValue) {
        try {
            return Instant.parse(rawValue);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("params.at must be ISO-8601 timestamp");
        }
    }
}
