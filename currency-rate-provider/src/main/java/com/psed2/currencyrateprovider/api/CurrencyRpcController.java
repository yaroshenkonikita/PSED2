package com.psed2.currencyrateprovider.api;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import com.psed2.currencyrateprovider.service.CurrencyRateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class CurrencyRpcController {
    private final CurrencyRateService currencyRateService;

    public CurrencyRpcController(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @PostMapping("/rpc")
    public JsonRpcResponse handle(@RequestBody(required = false) JsonRpcRequest request) {
        if (request == null || request.method() == null || !"2.0".equals(request.jsonrpc())) {
            return JsonRpcResponse.failure(-32600, "Invalid Request", request == null ? null : request.id());
        }

        if (!"getUsdRubRate".equals(request.method())) {
            return JsonRpcResponse.failure(-32601, "Method not found", request.id());
        }

        RateResult result = new RateResult(
                "USD/RUB",
                currencyRateService.currentUsdRubRate(),
                Instant.now().toString()
        );

        return JsonRpcResponse.success(result, request.id());
    }
}
