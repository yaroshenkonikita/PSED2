package com.psed2.currencyrateprovider.service.rpc;

import java.time.Instant;

public record RateQuery(
        String pair,
        Instant at
) {
}
