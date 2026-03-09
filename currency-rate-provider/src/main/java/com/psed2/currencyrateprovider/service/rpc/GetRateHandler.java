package com.psed2.currencyrateprovider.service.rpc;

import com.psed2.currencyrateprovider.api.RateResult;
import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;
import com.psed2.currencyrateprovider.service.CurrencyRateService;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class GetRateHandler implements RpcMethodHandler {
    private final CurrencyRateService currencyRateService;
    private final RateQueryExtractor rateQueryExtractor;

    public GetRateHandler(CurrencyRateService currencyRateService, RateQueryExtractor rateQueryExtractor) {
        this.currencyRateService = currencyRateService;
        this.rateQueryExtractor = rateQueryExtractor;
    }

    @Override
    public String methodName() {
        return "getRate";
    }

    @Override
    public JsonRpcResponse handle(JsonRpcRequest request) {
        try {
            return JsonRpcResponse.success(createResult(extractQuery(request)), request.id());
        } catch (IllegalArgumentException ex) {
            return invalidParams(ex, request);
        }
    }

    private RateQuery extractQuery(JsonRpcRequest request) {
        return rateQueryExtractor.extract(request.params());
    }

    private RateResult createResult(RateQuery query) {
        return new RateResult(
                query.pair().toUpperCase(Locale.ROOT),
                currencyRateService.currentRate(query.pair(), query.at()),
                query.at().toString()
        );
    }

    private JsonRpcResponse invalidParams(IllegalArgumentException exception, JsonRpcRequest request) {
        return JsonRpcResponse.failure(JsonRpcErrorCodes.INVALID_PARAMS, exception.getMessage(), request.id());
    }
}
