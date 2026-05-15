package com.psed2.currencyrateprovider.api;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import com.psed2.currencyrateprovider.service.rpc.JsonRpcDispatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyRpcController {
    private final JsonRpcDispatcher jsonRpcDispatcher;

    public CurrencyRpcController(JsonRpcDispatcher jsonRpcDispatcher) {
        this.jsonRpcDispatcher = jsonRpcDispatcher;
    }

    @PostMapping("/api/v1/rpc")
    public JsonRpcResponse handle(@RequestBody(required = false) JsonRpcRequest request) {
        return jsonRpcDispatcher.handle(request);
    }
}
