package com.psed2.currencyrateprovider.api;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import com.psed2.currencyrateprovider.observability.RpcServerMetrics;
import com.psed2.currencyrateprovider.service.rpc.JsonRpcDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
public class CurrencyRpcController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyRpcController.class);

    private final JsonRpcDispatcher jsonRpcDispatcher;
    private final RpcServerMetrics rpcServerMetrics;

    public CurrencyRpcController(JsonRpcDispatcher jsonRpcDispatcher, RpcServerMetrics rpcServerMetrics) {
        this.jsonRpcDispatcher = jsonRpcDispatcher;
        this.rpcServerMetrics = rpcServerMetrics;
    }

    @PostMapping("/api/v1/rpc")
    public JsonRpcResponse handle(
            @RequestBody(required = false) JsonRpcRequest request,
            @RequestHeader(value = "X-Client-Name", required = false) String clientName
    ) {
        Instant startedAt = Instant.now();
        logRequest(clientName, request);
        try {
            JsonRpcResponse response = dispatch(request);
            logResponse(clientName, response);
            rpcServerMetrics.recordSuccess(clientName, methodName(request), elapsedSince(startedAt));
            return response;
        } catch (RuntimeException ex) {
            rpcServerMetrics.recordServerError(clientName, methodName(request), elapsedSince(startedAt));
            throw ex;
        }
    }

    private JsonRpcResponse dispatch(JsonRpcRequest request) {
        return jsonRpcDispatcher.handle(request);
    }

    private void logRequest(String clientName, JsonRpcRequest request) {
        LOGGER.info("Server request from client={}: {}", clientName, request);
    }

    private void logResponse(String clientName, JsonRpcResponse response) {
        LOGGER.info("Server response to client={}: {}", clientName, response);
    }

    private Duration elapsedSince(Instant startedAt) {
        return Duration.between(startedAt, Instant.now());
    }

    private String methodName(JsonRpcRequest request) {
        return request == null ? null : request.method();
    }
}
