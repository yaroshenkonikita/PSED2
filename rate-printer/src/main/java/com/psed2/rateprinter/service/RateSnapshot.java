package com.psed2.rateprinter.service;

public record RateSnapshot(
        String pair,
        double rate,
        String at
) {
}
