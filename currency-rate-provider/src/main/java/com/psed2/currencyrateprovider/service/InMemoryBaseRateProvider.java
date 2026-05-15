package com.psed2.currencyrateprovider.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryBaseRateProvider implements BaseRateProvider {
    private static final Map<String, Double> BASE_RATES = Map.of(
            "USD/RUB", 90.0,
            "EUR/RUB", 98.0,
            "CNY/RUB", 12.5
    );

    @Override
    public double getBaseRate(String normalizedPair) {
        Double baseRate = BASE_RATES.get(normalizedPair);
        if (baseRate == null) {
            throw new IllegalArgumentException("Unsupported currency pair: " + normalizedPair);
        }
        return baseRate;
    }
}
