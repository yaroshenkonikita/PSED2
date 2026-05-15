package com.psed2.currencyrateprovider.observability;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RpcServerLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerLogger.class);

    public void logRequest(String clientName, JsonRpcRequest request) {
        LOGGER.info("Server request from client={}: {}", clientName, request);
    }

    public void logResponse(String clientName, JsonRpcResponse response) {
        LOGGER.info("Server response to client={}: {}", clientName, response);
    }

    public void logFailure(String clientName, RuntimeException exception) {
        LOGGER.warn("Server request from client={} failed: {}", clientName, exception.getMessage());
    }
}
