package com.psed2.rateprinter.service;

import java.time.Instant;
import java.util.Optional;

public interface ProviderRateClient {
    Optional<RateSnapshot> getRate(String pair, Instant at);
}
