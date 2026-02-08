package com.psed2.currencyrateprovider.api;

public record RateResult(
        String pair,
        double rate,
        String at
) {
}
