package com.psed2.currencyrateprovider.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RateValueCalculator {
    private static final double RANDOM_DELTA = 1.5;
    private static final int RATE_SCALE = 4;

    public double calculate(double baseRate, Instant at) {
        double value = baseRate + timeOffset(at) + randomOffset();
        return round(value);
    }

    private double timeOffset(Instant at) {
        long timeBucket = at.getEpochSecond() / 60;
        return (timeBucket % 7) * 0.01;
    }

    private double randomOffset() {
        return ThreadLocalRandom.current().nextDouble(-RANDOM_DELTA, RANDOM_DELTA);
    }

    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(RATE_SCALE, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
