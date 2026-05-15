package com.psed2.currencyrateprovider.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RpcServerMetrics {
    private static final String UNKNOWN_CLIENT = "unknown";
    private static final String UNKNOWN_METHOD = "unknown";
    private static final double MEDIAN = 0.5;
    private static final double PERCENTILE_95 = 0.95;
    private static final double PERCENTILE_99 = 0.99;

    private final MeterRegistry meterRegistry;

    public RpcServerMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordSuccess(String client, String method, Duration duration) {
        incrementRequestCounter(client, method);
        recordDuration(client, method, duration);
    }

    public void recordServerError(String client, String method, Duration duration) {
        incrementRequestCounter(client, method);
        incrementServerErrorCounter(client, method);
        recordDuration(client, method, duration);
    }

    private void incrementRequestCounter(String client, String method) {
        Counter.builder("rpc.server.requests")
                .description("JSON-RPC server requests")
                .tags("client", normalize(client), "method", normalizeMethod(method))
                .register(meterRegistry)
                .increment();
    }

    private void incrementServerErrorCounter(String client, String method) {
        Counter.builder("rpc.server.errors")
                .description("JSON-RPC server HTTP 500 errors")
                .tags("client", normalize(client), "method", normalizeMethod(method), "status", "500")
                .register(meterRegistry)
                .increment();
    }

    private void recordDuration(String client, String method, Duration duration) {
        Timer.builder("rpc.server.request.duration")
                .description("JSON-RPC server request processing time")
                .tags("client", normalize(client), "method", normalizeMethod(method))
                .publishPercentiles(MEDIAN, PERCENTILE_95, PERCENTILE_99)
                .publishPercentileHistogram()
                .register(meterRegistry)
                .record(duration);
    }

    private String normalize(String client) {
        return client == null || client.isBlank() ? UNKNOWN_CLIENT : client;
    }

    private String normalizeMethod(String method) {
        return method == null || method.isBlank() ? UNKNOWN_METHOD : method;
    }
}
