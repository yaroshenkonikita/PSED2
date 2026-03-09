package com.psed2.currencyrateprovider.service;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CurrencyPairNormalizer {

    public String normalize(String pair) {
        if (pair == null || pair.isBlank()) {
            throw new IllegalArgumentException("Currency pair is required");
        }
        return pair.trim().toUpperCase(Locale.ROOT);
    }
}
