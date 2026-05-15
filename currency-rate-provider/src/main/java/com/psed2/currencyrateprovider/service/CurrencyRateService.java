package com.psed2.currencyrateprovider.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CurrencyRateService {
    private final CurrencyPairNormalizer pairNormalizer;
    private final BaseRateProvider baseRateProvider;
    private final RateValueCalculator rateValueCalculator;

    public CurrencyRateService(
            CurrencyPairNormalizer pairNormalizer,
            BaseRateProvider baseRateProvider,
            RateValueCalculator rateValueCalculator
    ) {
        this.pairNormalizer = pairNormalizer;
        this.baseRateProvider = baseRateProvider;
        this.rateValueCalculator = rateValueCalculator;
    }

    public double currentRate(String pair, Instant at) {
        String normalizedPair = pairNormalizer.normalize(pair);
        double baseRate = baseRateProvider.getBaseRate(normalizedPair);
        return rateValueCalculator.calculate(baseRate, at);
    }
}
