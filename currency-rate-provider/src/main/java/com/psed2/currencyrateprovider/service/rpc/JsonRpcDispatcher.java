package com.psed2.currencyrateprovider.service.rpc;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JsonRpcDispatcher {
    private static final String JSON_RPC_VERSION = "2.0";

    private final Map<String, RpcMethodHandler> handlersByMethod;

    public JsonRpcDispatcher(List<RpcMethodHandler> handlers) {
        handlersByMethod = handlers.stream()
                .collect(Collectors.toMap(RpcMethodHandler::methodName, Function.identity()));
    }

    public JsonRpcResponse handle(JsonRpcRequest request) {
        if (isInvalidRequest(request)) {
            return JsonRpcResponse.failure(JsonRpcErrorCodes.INVALID_REQUEST, "Invalid Request", requestId(request));
        }

        RpcMethodHandler handler = findHandler(request);
        if (handler == null) {
            return JsonRpcResponse.failure(JsonRpcErrorCodes.METHOD_NOT_FOUND, "Method not found", request.id());
        }

        return handler.handle(request);
    }

    private boolean isInvalidRequest(JsonRpcRequest request) {
        return request == null
                || request.method() == null
                || !JSON_RPC_VERSION.equals(request.jsonrpc());
    }

    private Object requestId(JsonRpcRequest request) {
        return request == null ? null : request.id();
    }

    private RpcMethodHandler findHandler(JsonRpcRequest request) {
        return handlersByMethod.get(request.method());
    }
}
