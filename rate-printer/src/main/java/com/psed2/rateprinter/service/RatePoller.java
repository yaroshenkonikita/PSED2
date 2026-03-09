package com.psed2.rateprinter.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.Clock;
import java.time.Instant;

@Component
public class RatePoller {
    private final ProviderRateClient providerRateClient;
    private final RateOutputWriter rateOutputWriter;
    private final Clock clock;
    private final String pair;

    public RatePoller(
            ProviderRateClient providerRateClient,
            RateOutputWriter rateOutputWriter,
            Clock clock,
            @Value("${poll.pair:USD/RUB}") String pair
    ) {
        this.providerRateClient = providerRateClient;
        this.rateOutputWriter = rateOutputWriter;
        this.clock = clock;
        this.pair = pair;
    }

    @Scheduled(
            fixedDelayString = "${poll.interval.ms:5000}",
            initialDelayString = "${poll.initial-delay.ms:1000}"
    )
    public void printRate() {
        providerRateClient.getRate(pair, now())
                .ifPresent(rateOutputWriter::write);
    }

    private Instant now() {
        return Instant.now(clock);
    }
}
