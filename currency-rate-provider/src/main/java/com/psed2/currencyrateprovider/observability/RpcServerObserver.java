package com.psed2.currencyrateprovider.observability;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class RpcServerObserver {
    private final RpcServerLogger rpcServerLogger;
    private final RpcServerMetrics rpcServerMetrics;

    public RpcServerObserver(RpcServerLogger rpcServerLogger, RpcServerMetrics rpcServerMetrics) {
        this.rpcServerLogger = rpcServerLogger;
        this.rpcServerMetrics = rpcServerMetrics;
    }

    public RpcRequestObservation requestStarted(String clientName, JsonRpcRequest request) {
        rpcServerLogger.logRequest(clientName, request);
        return new RpcRequestObservation(clientName, methodName(request), Instant.now());
    }

    public void requestSucceeded(RpcRequestObservation observation, JsonRpcResponse response) {
        rpcServerLogger.logResponse(observation.clientName(), response);
        rpcServerMetrics.recordSuccess(observation.clientName(), observation.methodName(), elapsedSince(observation));
    }

    public void requestFailed(RpcRequestObservation observation, RuntimeException exception) {
        rpcServerLogger.logFailure(observation.clientName(), exception);
        rpcServerMetrics.recordServerError(observation.clientName(), observation.methodName(), elapsedSince(observation));
    }

    private String methodName(JsonRpcRequest request) {
        return request == null ? null : request.method();
    }

    private Duration elapsedSince(RpcRequestObservation observation) {
        return Duration.between(observation.startedAt(), Instant.now());
    }
}
