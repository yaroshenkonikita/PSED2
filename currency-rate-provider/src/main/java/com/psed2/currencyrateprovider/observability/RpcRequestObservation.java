package com.psed2.currencyrateprovider.observability;

import java.time.Instant;

public record RpcRequestObservation(
        String clientName,
        String methodName,
        Instant startedAt
) {
}
