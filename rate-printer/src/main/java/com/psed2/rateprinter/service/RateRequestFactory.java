package com.psed2.rateprinter.service;

import com.psed2.rateprinter.rpc.GetRateParams;
import com.psed2.rateprinter.rpc.JsonRpcRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RateRequestFactory {
    private static final String JSON_RPC_VERSION = "2.0";
    private static final String GET_RATE_METHOD = "getRate";
    private static final int REQUEST_ID = 1;

    public JsonRpcRequest create(String pair, Instant at) {
        return new JsonRpcRequest(
                JSON_RPC_VERSION,
                GET_RATE_METHOD,
                new GetRateParams(pair, at.toString()),
                REQUEST_ID
        );
    }
}
