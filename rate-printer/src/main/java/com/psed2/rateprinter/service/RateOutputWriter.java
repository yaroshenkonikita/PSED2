package com.psed2.rateprinter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RateOutputWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateOutputWriter.class);

    public void write(RateSnapshot snapshot) {
        LOGGER.info("{} = {} at {}", snapshot.pair(), formatRate(snapshot.rate()), snapshot.at());
    }

    private String formatRate(double rate) {
        return String.format(Locale.ROOT, "%.4f", rate);
    }
}
