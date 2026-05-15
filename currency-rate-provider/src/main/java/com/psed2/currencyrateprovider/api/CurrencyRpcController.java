package com.psed2.currencyrateprovider.api;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import com.psed2.currencyrateprovider.observability.RpcRequestObservation;
import com.psed2.currencyrateprovider.observability.RpcServerObserver;
import com.psed2.currencyrateprovider.service.rpc.JsonRpcDispatcher;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyRpcController {
    private final JsonRpcDispatcher jsonRpcDispatcher;
    private final RpcServerObserver rpcServerObserver;

    public CurrencyRpcController(JsonRpcDispatcher jsonRpcDispatcher, RpcServerObserver rpcServerObserver) {
        this.jsonRpcDispatcher = jsonRpcDispatcher;
        this.rpcServerObserver = rpcServerObserver;
    }

    @PostMapping("/api/v1/rpc")
    public JsonRpcResponse handle(
            @RequestBody(required = false) JsonRpcRequest request,
            @RequestHeader(value = "X-Client-Name", required = false) String clientName
    ) {
        RpcRequestObservation observation = rpcServerObserver.requestStarted(clientName, request);
        try {
            JsonRpcResponse response = dispatch(request);
            rpcServerObserver.requestSucceeded(observation, response);
            return response;
        } catch (RuntimeException ex) {
            rpcServerObserver.requestFailed(observation, ex);
            throw ex;
        }
    }

    private JsonRpcResponse dispatch(JsonRpcRequest request) {
        return jsonRpcDispatcher.handle(request);
    }
}
