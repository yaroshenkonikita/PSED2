package com.psed2.currencyrateprovider.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CurrencyRateService {
    private static final double BASE_USD_RUB = 90.0;
    private static final double RANDOM_DELTA = 1.5;

    public double currentUsdRubRate() {
        double value = BASE_USD_RUB + ThreadLocalRandom.current().nextDouble(-RANDOM_DELTA, RANDOM_DELTA);
        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP).doubleValue();
    }
}
